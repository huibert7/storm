package org.storm.filter;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

import org.storm.filter.FilePart;
import org.storm.filter.ParamPart;
import org.storm.filter.Part;

@WebFilter(
        urlPatterns = "/*",
        initParams = @WebInitParam(name = "tempDirectory", value = "/Users/huibert/Downloads/")       
)
public final class FileUploadFilter implements Filter
{
	private FilterConfig				filterConfig = null;
	private String						boundary;
	private ServletInputStream			in;
	private FilePart					lastFilePart;
	private byte[] 						buf = new byte[8 * 1024];
	private Part						tempPart;
	private UploadRequestWrapper		theWrapper = null;
	private String						path = null;
	private Vector<String>				theFiles = new Vector<String>();
	private File						tempFile;
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		Enumeration<String> e;
		String 		type = request.getContentType();

		if (type == null || !type.toLowerCase().startsWith("multipart/form-data"))
		{
			chain.doFilter(request,response);
		}
		else
		{
			boundary = extractBoundary(type);

			if (boundary == null)
			{
				throw new ServletException("Separation boundary was not specified");
			}

			in = ((HttpServletRequest)request).getInputStream();
			
			try
			{
				path = filterConfig.getInitParameter("tempDirectory");

				String line = readLine();

				if (line == null || !line.startsWith(boundary))
				{
					chain.doFilter(request,response);
//					 We could throw new ServletException("Corrupt form data: no leading boundary"); if !line.startsWith(boundary)
				}
				else
				{
					theWrapper = new UploadRequestWrapper((HttpServletRequest)request);
					do
					{
						tempPart = readNextPart();
					
						if (tempPart != null)
						{
							if (tempPart instanceof ParamPart)
							{
								theWrapper.setParameter(tempPart.getName(),((ParamPart)tempPart).getStringValue());
							}
							else
							{
								if (((FilePart)tempPart).getFileName() != null)
								{
									theWrapper.setParameter (tempPart.getName(),path+((HttpServletRequest)request).getSession().getId()+((FilePart)tempPart).getFileName());
									theFiles.addElement(((HttpServletRequest)request).getSession().getId()+((FilePart)tempPart).getFileName());
									((FilePart)tempPart).writeTo(new File(path,((HttpServletRequest)request).getSession().getId()+((FilePart)tempPart).getFileName()));
								}
							}
						}
					}
					while (tempPart != null);

					chain.doFilter(theWrapper,response);
					
					e = theFiles.elements();
					while (e.hasMoreElements())
					{
						tempFile = new File(path+(String)e.nextElement());
						tempFile.delete();
					}
				}
			}
			catch (IOException ex)
			{
				System.out.println("Error in Filter... Exception: " + ex.getMessage() + "\n" + ex.toString());
			}
		}
	}

	public FilterConfig getFilterConfig()
	{
		return filterConfig;
	}

  /**
   * Read the next part arriving in the stream. Will be either a
   * <code>FilePart</code> or a <code>ParamPart</code>, or <code>null</code>
   * to indicate there are no more parts to read. The order of arrival
   * corresponds to the order of the form elements in the submitted form.
   *
   * @return either a <code>FilePart</code>, a <code>ParamPart</code> or
   *        <code>null</code> if there are no more parts to read.
   * @exception IOException	if an input or output exception has occurred.
   *
   * @see FilePart
   * @see ParamPart
   */
	public Part readNextPart() throws IOException,ServletException
	{
		// Make sure the last file was entirely read from the input
		if (lastFilePart != null)
		{
			lastFilePart.getInputStream().close();
			lastFilePart = null;
		}
    
		// Read the first line, should look like this:
		// content-disposition: form-data; name="field1"; filename="file1.txt"
		String line = readLine();
		if (line == null)
		{
			// No parts left, we're done
			return null;
		}
		else if (line.length() == 0)
		{
			// IE4 on Mac sends an empty line at the end; treat that as the end.
			// Thanks to Daniel Lemire and Henri Tourigny for this fix.
			return null;
		}

		// Parse the content-disposition line
		String[] dispInfo = extractDispositionInfo(line);
		// String disposition = dispInfo[0];  // not currently used
		String name = dispInfo[1];
		String filename = dispInfo[2];

		// Now onto the next line.  This will either be empty
		// or contain a Content-Type and then an empty line.
		line = readLine();
		if (line == null)
		{
			// No parts left, we're done
			return null;
		}

	    // Get the content type, or null if none specified
	    String contentType = extractContentType(line);
		if (contentType != null)
		{
			// Eat the empty line
			line = readLine();
			if (line == null || line.length() > 0)
			{
				// line should be empty
				throw new IOException("Malformed line after content type: " + line);
			}
		}
		else
		{
			// Assume a default content type, rfc1867 says that's text/plain
			contentType = "text/plain";
		}
	
		// Now, finally, we read the content (end after reading the boundary)
		if (filename == null)
		{
			// This is a parameter, add it to the vector of values
			return new ParamPart(name, in, boundary);
		}
		else
		{
			// This is a file
			if (filename.equals(""))
			{
				filename = null; // empty filename, probably an "empty" file param
			}
			lastFilePart = new FilePart( name, in, boundary, contentType, filename);
			return lastFilePart;
		}
	}

   /**
	* Extracts and returns the boundary token from a line.
	*
	* @return the boundary token.
	*/
	private String extractBoundary(String line)
	{
		// Use lastIndexOf() because IE 4.01 on Win98 has been known to send the
		// "boundary=" string multiple times.  Thanks to David Wall for this fix.

		int index = line.lastIndexOf("boundary=");

		if (index == -1) return null;

		String boundary = line.substring(index + 9);  // 9 for "boundary="

		// The real boundary is always preceeded by an extra "--"

		boundary = "--" + boundary;

		return boundary;
	}

   /**
	* Extracts and returns disposition info from a line, as a <code>String<code>
	* array with elements: disposition, name, filename.
	*
	* @return String[] of elements: disposition, name, filename.
	* @exception  IOException if the line is malformatted.
	*/
	private String[] extractDispositionInfo(String line) throws IOException
	{
		// Return the line's data as an array: disposition, name, filename
		String[] retval = new String[3];

		// Convert the line to a lowercase string without the ending \r\n
		// Keep the original line for error messages and for variable names.
		String origline = line;
		line = origline.toLowerCase();

		// Get the content disposition, should be "form-data"
		int start = line.indexOf("content-disposition: ");
		int end = line.indexOf(";");
		if (start == -1 || end == -1)
		{
			throw new IOException("Content disposition corrupt: " + origline);
		}
		String disposition = line.substring(start + 21, end);
		if (!disposition.equals("form-data"))
		{
			throw new IOException("Invalid content disposition: " + disposition);
		}

		// Get the field name
		start = line.indexOf("name=\"", end);  // start at last semicolon
		end = line.indexOf("\"", start + 7);   // skip name=\"
		if (start == -1 || end == -1)
		{
			throw new IOException("Content disposition corrupt: " + origline);
		}
		String name = origline.substring(start + 6, end);

		// Get the filename, if given
		String filename = null;
		start = line.indexOf("filename=\"", end + 2);  // start after name
		end = line.indexOf("\"", start + 10);          // skip filename=\"
		if (start != -1 && end != -1)                  // note the !=
		{
			filename = origline.substring(start + 10, end);
			// The filename may contain a full path.  Cut to just the filename.
			int slash = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
			if (slash > -1) filename = filename.substring(slash + 1);  // past last slash
		}

		// Return a String array: disposition, name, filename
		// empty filename denotes no file posted!
		retval[0] = disposition;
		retval[1] = name;
		retval[2] = filename;

		return retval;
	}

   /**
	* Extracts and returns the content type from a line, or null if the
	* line was empty.
	*
	* @return content type, or null if line was empty.
	* @exception  IOException if the line is malformatted.
	*/
	private String extractContentType(String line) throws IOException,ServletException
	{
		String contentType = null;

		// Convert the line to a lowercase string
		String origline = line;
		line = origline.toLowerCase();

		// Get the content type, if any
		if (line.startsWith("content-type"))
		{
			int start = line.indexOf(" ");
			if (start == -1) throw new ServletException("Content type corrupt: " + origline);

			contentType = line.substring(start + 1);
		}
		else if (line.length() != 0)
		{
			// no content type, so should be empty
			throw new IOException("Malformed line after disposition: " + origline);
		}

		return contentType;
	}
  
   /**
	* Read the next line of input.
	*
	* @return  a String containing the next line of input from the stream,
	*          or null to indicate the end of the stream.
	* @exception IOException	if an input or output exception has occurred.
    */
	private String readLine() throws IOException
	{
		StringBuffer sbuf = new StringBuffer();
		int result;

		do
		{
			result = in.readLine(buf, 0, buf.length);  // does +=
			if (result != -1)
			{
				sbuf.append(new String(buf, 0, result, "ISO-8859-1"));
			}
		}
		while (result == buf.length);  // loop only if the buffer was filled

		if (sbuf.length() == 0) return null;  // nothing read, must be at the end of stream

		sbuf.setLength(sbuf.length() - 2);  // cut off the trailing \r\n
		return sbuf.toString();
	}


    public void destroy()
    {
    }

    public void init(FilterConfig filterConfig)
    {
		this.filterConfig = filterConfig;
    }
}

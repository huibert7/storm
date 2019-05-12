package org.storm.tags.general.utilities.encryption;

import org.storm.tags.StormTag;
import org.storm.util.encryption.BinConverter;
import org.storm.util.encryption.BlowfishCBC;
import org.storm.util.encryption.BlowfishECB;
import org.storm.util.encryption.SHA1;

public class DecryptTag extends StormTag
{
	private static final long serialVersionUID = 5135243292406321006L;
	String		key = "9158b610a71b06e56f2ce39ef061f7ed13ce89a1";
	String		string;
	SHA1		s = null;
	BlowfishCBC	blowfishCBCEngine;

	public boolean supportsSuccess()
	{
		return true;
	}

	public boolean supportsError()
	{
		return true;
	}

	public void setString(String tempParam)
	{
		if (tempParam == null || tempParam.equals("null"))
		{
			error = true;
			errorMsg = "Missing 'string' parameter";
			errorCode = -1501;
		}
		else
		{
			string = tempParam;
		}
	}

	public void setPassword(String tempParam)
	{
		if (!(tempParam == null || tempParam.equals("null")))
		{
			s = new SHA1();

			if (!s.selfTest())
			{
				error = true;
				errorMsg = "Failed self-test";
				errorCode = -1502;
			}
			else
			{
				s.update(tempParam);
				s.finalize();
				key = s.toString();
				s.clear();
			}

			s = null;
		}
	}

	public int doStartTag()
	{
		blowfishCBCEngine = new BlowfishCBC(key.getBytes(),0);

		// get the number of estimated bytes in the string (cut off broken blocks)
		int nLen = (string.length() >> 1) & ~7;

		// does the given stuff make sense (at least the CBC IV)?
		if (nLen < BlowfishECB.BLOCKSIZE)
		{
			error = true;
			errorMsg = "The 'string' parameter does not contain a valid encrypted string";
			errorCode = -1504;
		}

		if (!error)
		{
			// get the CBC IV
			byte[]	cbciv = new byte[BlowfishCBC.BLOCKSIZE];
			int		nNumOfBytes = BinConverter.binHexToBytes(string,cbciv,0,0,BlowfishCBC.BLOCKSIZE);

			if (nNumOfBytes < BlowfishCBC.BLOCKSIZE)
			{
				error = true;
				errorMsg = "The 'string' parameter does not contain a valid encrypted string";
				errorCode = -1504;
			}

			if (!error)
			{
				// (got it)
				blowfishCBCEngine.setCBCIV(cbciv);

				// something left to decrypt?       
				nLen -= BlowfishCBC.BLOCKSIZE;
				if (nLen == 0) 
				{
					string = "";
				}
				else
				{
					// get all data bytes now
					byte[] buf = new byte[nLen];

					nNumOfBytes = BinConverter.binHexToBytes(string,buf,BlowfishCBC.BLOCKSIZE * 2,0,nLen);

					// we cannot accept broken binhex sequences due to padding
					// and decryption
					if (nNumOfBytes < nLen)
					{
						error = true;
						errorMsg = "The 'string' parameter does not contain a valid encrypted string";
						errorCode = -1504;
					}
					else
					{
						// decrypt the buffer
						blowfishCBCEngine.decrypt(buf);

						// get the last padding byte
						int nPadByte = (int)buf[buf.length - 1] & 0x0ff;

						// ( try to get all information if the padding doesn't seem to be correct)
						if ((nPadByte > 8) || (nPadByte < 0))
						{
							nPadByte = 0;
						}

						// calculate the real size of this message
						nNumOfBytes -= nPadByte;
						if (nNumOfBytes < 0)
						{
							string = "";
						}
						else
						{
							// success
							string = BinConverter.byteArrayToUNCString(buf,0,nNumOfBytes);
						}
					}
				}
			}
		}

		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		if (!error)
		{
			pageContext.setAttribute("decryptedString",string);

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		return EVAL_PAGE;
	}
}

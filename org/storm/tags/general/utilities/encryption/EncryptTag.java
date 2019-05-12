package org.storm.tags.general.utilities.encryption;

import java.util.Random;

import org.storm.tags.StormTag;
import org.storm.util.encryption.BinConverter;
import org.storm.util.encryption.BlowfishCBC;
import org.storm.util.encryption.SHA1;

public class EncryptTag extends StormTag
{
	private static final long serialVersionUID = 2633109836751999493L;
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

	public int doStartTag()
	{
		long	lCBCIV;
		Random	m_rndGen = new Random();

		blowfishCBCEngine = new BlowfishCBC(key.getBytes(),0);

		synchronized (m_rndGen)
		{
			lCBCIV = m_rndGen.nextLong();
		}

		// allocate the buffer (align to the next 8 byte border plus padding)
		int nStrLen = string.length();
		byte[] buf = new byte [((nStrLen << 1) & 0xfffffff8) + 8];

		// copy all bytes of the string into the buffer (use network byte order)
		int nI;
		int nPos = 0;
		for (nI = 0; nI < nStrLen; nI++)
		{
			char cActChar = string.charAt(nI);
			buf[nPos++] = (byte) ((cActChar >> 8) & 0x0ff);
			buf[nPos++] = (byte) (cActChar & 0x0ff);
		}

		// pad the rest with the PKCS5 scheme
		byte bPadVal = (byte)(buf.length - (nStrLen << 1));
		while (nPos < buf.length)
		{
			buf[nPos++] = bPadVal;
		}

		// create the encryptor
		blowfishCBCEngine.setCBCIV(lCBCIV);

		// encrypt the buffer
		blowfishCBCEngine.encrypt(buf);

		// return the binhex string
		byte[] newCBCIV = new byte[BlowfishCBC.BLOCKSIZE];
		BinConverter.longToByteArray(lCBCIV,newCBCIV,0);

		string = BinConverter.bytesToBinHex(newCBCIV,0,BlowfishCBC.BLOCKSIZE) + 
					BinConverter.bytesToBinHex(buf, 0, buf.length);

		blowfishCBCEngine.cleanUp();

		return EVAL_BODY_AGAIN;
	}

	public int doSuccess()
	{
		if (!error)
		{
			pageContext.setAttribute("encryptedString",string);

			return EVAL_BODY_AGAIN;
		}
		return SKIP_BODY;
	}

	public int doEndTag()
	{
		key = "9158b610a71b06e56f2ce39ef061f7ed13ce89a1";
		
		return EVAL_PAGE;
	}
}

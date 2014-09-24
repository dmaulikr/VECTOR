package physics;

public class Configuration
{
	private int frameWidth = 900;
	private int frameHeight = 600;

	private double[] anglesA = {dr(0), dr(0), dr(0)};
	private double[] magnitudesA = {0, 0, 0};
	private double[] xsA = {frameWidth * 1 / 3, frameWidth * 1 / 3, frameWidth * 1 / 3};
	private double[] ysA = {frameHeight * 1 / 4, frameHeight * 2 / 4, frameHeight * 3 / 4};

	private double[] anglesB = {dr(0), dr(0), dr(0)};
	private double[] magnitudesB = {0, 0, 0};
	private double[] xsB = {frameWidth * 2 / 3, frameWidth * 2 / 3, frameWidth * 2 / 3};
	private double[] ysB = {frameHeight * 1 / 4, frameHeight * 2 / 4, frameHeight * 3 / 4};

	public Configuration(int width, int height)
	{
		this.frameWidth = width;
		this.frameHeight = height;
	}


	private double dr(double angle)
	{
		return angle * Math.PI / 180;
	}

	private double rd(double angle)
	{
		return angle / Math.PI * 180;
	}

	public void setAngle(double angle, char aOrB, int index)
	{
		if(aOrB == 'a')
			anglesA[index] = angle;
		else if(aOrB == 'b')
			anglesB[index] = angle;
	}

	public double getAngle(char aOrB, int index)
	{
		if(aOrB == 'a')
			return anglesA[index];
		else if(aOrB == 'b')
			return anglesB[index];
		else
			throw new ConfigurationException("Not A or B");
	}

	public void setMagnitude(double magnitude, char aOrB, int index)
	{
		if(aOrB == 'a')
			magnitudesA[index] = magnitude;
		else if(aOrB == 'b')
			magnitudesB[index] = magnitude;
	}

	public double getMagnitude(char aOrB, int index)
	{
		if(aOrB == 'a')
			return magnitudesA[index];
		else if(aOrB == 'b')
			return magnitudesB[index];
		else
			throw new ConfigurationException("Not A or B");
	}

	public double getComponent(char xOrY, char aOrB, int index)
	{
		if(aOrB == 'a')
		{
			if(xOrY == 'x')
				return magnitudesA[index] * Math.cos(anglesA[index]);
			else if(xOrY == 'y')
				return magnitudesA[index] * Math.sin(anglesA[index]);
			else
				throw new ConfigurationException("Not X or Y");
		}
		else if(aOrB == 'b')
		{
			if(xOrY == 'x')
				return magnitudesB[index] * Math.cos(anglesB[index]);
			else if(xOrY == 'y')
				return magnitudesB[index] * Math.sin(anglesB[index]);
			else
				throw new ConfigurationException("Not A or B");
		}
		else
			throw new ConfigurationException("Not X or Y");
	}

	public void setAngleByComponent(double x, double y, char aOrB, int index)
	{
		if(aOrB == 'a')
		{
			anglesA[index] = Math.tan(y / x);
		}
		else if(aOrB == 'b')
		{
			anglesB[index] = Math.tan(y / x);
		}
		else
			throw new ConfigurationException("Not A or B");
	}

	public void setXandY(double x, double y, char aOrB, int index)
	{
		if(aOrB == 'a')
		{
			xsA[index] = x;
			ysA[index] = y;
		}
		else if(aOrB == 'b')
		{
			xsB[index] = x;
			ysB[index] = y;
		}
	}

	public double getXorY(char xOrY, char aOrB, int index)
	{
		if(xOrY == 'x')
		{
			if(aOrB == 'a')
				return xsA[index];
			else if(aOrB == 'b')
				return xsB[index];
			else
				throw new ConfigurationException("Not A or B");
		}
		else if(xOrY == 'y')
		{
			if(aOrB == 'a')
				return ysA[index];
			else if(aOrB == 'b')
				return ysB[index];
			else
				throw new ConfigurationException("Not A or B");
		}
		else
			throw new ConfigurationException("Not X or Y");
	}

	public void setWithCode(String code)
	{
		String[] lines = code.split("\n");
		String[] line1 = lines[0].split(" ");
		String[] line1a = line1[0].split(",");
		String[] line1b = line1[1].split(",");
		String[] line1c = line1[2].split(",");
		String[] line2 = lines[1].split(" ");
		String[] line3 = lines[2].split(" ");
		String[] line4 = lines[3].split(" ");
		String[] line4a = line4[0].split(",");
		String[] line4b = line4[1].split(",");
		String[] line4c = line4[2].split(",");
		String[] line5 = lines[4].split(" ");
		String[] line6 = lines[5].split(" ");

		xsA[0] = Double.parseDouble(line1a[0]); ysA[0] = Double.parseDouble(line1a[1]);
		xsA[1] = Double.parseDouble(line1b[0]); ysA[1] = Double.parseDouble(line1b[1]);
		xsA[2] = Double.parseDouble(line1c[0]); ysA[2] = Double.parseDouble(line1c[1]);

		xsB[0] = Double.parseDouble(line4a[0]); ysB[0] = Double.parseDouble(line4a[1]);
		xsB[1] = Double.parseDouble(line4b[0]); ysB[1] = Double.parseDouble(line4b[1]);
		xsB[2] = Double.parseDouble(line4c[0]); ysB[2] = Double.parseDouble(line4c[1]);

		anglesA[0] = Double.parseDouble(line2[0]);
		anglesA[1] = Double.parseDouble(line2[1]);
		anglesA[2] = Double.parseDouble(line2[2]);

		magnitudesA[0] = Double.parseDouble(line3[0]);
		magnitudesA[1] = Double.parseDouble(line3[1]);
		magnitudesA[2] = Double.parseDouble(line3[2]);

		anglesB[0] = Double.parseDouble(line5[0]);
		anglesB[1] = Double.parseDouble(line5[1]);
		anglesB[2] = Double.parseDouble(line5[2]);

		magnitudesB[0] = Double.parseDouble(line6[0]);
		magnitudesB[1] = Double.parseDouble(line6[1]);
		magnitudesB[2] = Double.parseDouble(line6[2]);
	}

	public String toCode()
	{
		String out = "";

		for(int i = 0; i < xsA.length; i++)
			out += xsA[i] + "," + ysA[i] + " ";
		out = out.substring(0, out.length() - 1) + "\n";

		for(int i = 0; i < anglesA.length; i++)
			out += rd(anglesA[i]) + " ";
		out = out.substring(0, out.length() - 1) + "\n";

		for(int i = 0; i < magnitudesA.length; i++)
			out += magnitudesA[i] + " ";
		out = out.substring(0, out.length() - 1) + "\n";

		for(int i = 0; i < xsB.length; i++)
			out += xsB[i] + "," + ysB[i] + " ";
		out = out.substring(0, out.length() - 1) + "\n";

		for(int i = 0; i < anglesB.length; i++)
			out += rd(anglesB[i]) + " ";
		out = out.substring(0, out.length() - 1) + "\n";

		for(int i = 0; i < magnitudesB.length; i++)
			out += magnitudesB[i] + " ";
		out = out.substring(0, out.length() - 1);

		return out;
	}

	public String toString()
	{
		String out = "";

		out += "CoordA: {";
		for(int i = 0; i < xsA.length; i++)
			out += "(" + xsA[i] + ", " + ysA[i] + ") ";
		out = out.substring(0, out.length() - 1) + "} \n";
		out += "AnglesA: {";
		for(int i = 0; i < anglesA.length; i++)
			out += rd(anglesA[i]) + ", ";
		out = out.substring(0, out.length() - 2) + "} \n";
		out += "MagnitudesA: {";
		for(int i = 0; i < magnitudesA.length; i++)
			out += magnitudesA[i] + ", ";
		out = out.substring(0, out.length() - 2) + "}\n";

		out += "CoordB: {";
		for(int i = 0; i < xsB.length; i++)
			out += "(" + xsB[i] + ", " + ysB[i] + ") ";
		out = out.substring(0, out.length() - 1) + "} \n";
		out += "AnglesB: {";
		for(int i = 0; i < anglesB.length; i++)
			out += rd(anglesB[i]) + ", ";
		out = out.substring(0, out.length() - 2) + "} \n";
		out += "MagnitudesB: {";
		for(int i = 0; i < magnitudesB.length; i++)
			out += magnitudesB[i] + ", ";
		out = out.substring(0, out.length() - 2) + "}";

		return out;
	}

	public class ConfigurationException extends IndexOutOfBoundsException
	{
		private static final long serialVersionUID = 1L;
		private String message = "";
		public ConfigurationException()
		{
			super();
		}

		public ConfigurationException(String str)
		{
			super(str);
			message = str;
		}

		@Override
		public void printStackTrace()
		{
			System.err.println("ConfigurationException: " + message);
		}
	}
}
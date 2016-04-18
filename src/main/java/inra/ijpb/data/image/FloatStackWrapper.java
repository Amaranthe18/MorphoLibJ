/**
 * 
 */
package inra.ijpb.data.image;

import ij.ImageStack;
import inra.ijpb.data.Cursor3D;

/**
 * Access the data of a 3D image containing intensity values stored as float.
 * 
 * 
 * <p>
 * Example of use:
 *<pre>{@code
 *	ImageStack stack = IJ.getImage().getStack();
 *	Image3D image = new FloatStackWrapper(stack);
 *	double val = image.getValue(0, 0, 0);
 *}</pre>
 * 
 * @see ByteStackWrapper
 * @see ShortStackWrapper
 * 
 * @author David Legland
 *
 */
public class FloatStackWrapper implements Image3D
{
	float[][] slices;
	
	int sizeX;
	int sizeY;
	int sizeZ;
	
	public FloatStackWrapper(ImageStack stack) 
	{
		// Check type
		if (stack.getBitDepth() != 32) 
		{
			throw new IllegalArgumentException("Requires a 32-bits stack");
		}
		
		// store stack size
		this.sizeX = stack.getWidth();
		this.sizeY = stack.getHeight();
		this.sizeZ = stack.getSize();

		// Convert slices type
		this.slices = new float[sizeZ][];
		Object[] array = stack.getImageArray();
		for (int i = 0; i < sizeZ; i++) 
		{
			slices[i] = (float[]) array[i];
		}
	}
	
	@Override
	public int getSize(int dim)
	{
		switch(dim)
		{
		case 0: return this.sizeX;
		case 1: return this.sizeY;
		case 2: return this.sizeZ;
		default:
			throw new IllegalArgumentException("Dimension must be comprised between 0 and 2, not " + dim);
		}
	}

	/* (non-Javadoc)
	 * @see inra.ijpb.data.image.Image3D#get(int, int, int)
	 */
	@Override
	public int get(int x, int y, int z) 
	{
		return (int) slices[z][y * sizeX + x];
	}

	/* (non-Javadoc)
	 * @see inra.ijpb.data.image.Image3D#set(int, int, int, int)
	 */
	@Override
	public void set(int x, int y, int z, int value)
	{
		slices[z][y * sizeX + x] = (float) value;
	}

	/* (non-Javadoc)
	 * @see inra.ijpb.data.image.Image3D#getValue(int, int, int)
	 */
	@Override
	public double getValue(int x, int y, int z)
	{
		return slices[z][y * sizeX + x];
	}

	@Override
	public double getValue(Cursor3D pos)
	{
		return getValue(pos.getX(), pos.getY(), pos.getZ());
	}

	/* (non-Javadoc)
	 * @see inra.ijpb.data.image.Image3D#setValue(int, int, int, double)
	 */
	@Override
	public void setValue(int x, int y, int z, double value) 
	{
		slices[z][y * sizeX + x] = (float) value;
	}

	@Override
	public void setValue(Cursor3D pos, double value)
	{
		setValue(pos.getX(), pos.getY(), pos.getZ(), value);
	}

}

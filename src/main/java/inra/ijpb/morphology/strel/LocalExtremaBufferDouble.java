package inra.ijpb.morphology.strel;

/**
 * <p>
 * Computes the maximum in a local buffer around current point.
 * </p>
 * <p>
 * This implementation considers a circular buffer (when a value is added, 
 * it replaces the first value that was inserted) 
 * that makes it possible to update extrema if needed.
 * </p>
 * <p>
 * Works only for Grayscale images coded between 0 and 255.
 * </p>
 * 
 * @see LocalBufferMax
 * @see LocalBufferMin
 * @see LocalBufferedHistogram
 * @author David Legland
 *
 */
public class LocalExtremaBufferDouble {
	
	/**
	 * Current max value
	 */
	double maxValue = Double.MIN_VALUE;

	boolean updateNeeded = false;
	
	/**
	 * Use a sign flag for managing both min and max.
	 * sign = +1 -> compute max values
	 * sign = -1 -> compute min values
	 */
	int sign;
	
	/**
	 * Circular buffer of stored values
	 */
	double[] buffer;
	
	/**
	 * Current index in circular buffer
	 */
	int bufferIndex = 0;
	
	/**
	 * Main constructor.
	 */
	public LocalExtremaBufferDouble(int n) {
		this.buffer = new double[n];
		for (int i = 0; i < n; i++)
			this.buffer[i] = 0;
	}
	
	/**
	 * Initializes an histogram filled with the given value.
	 */
	public LocalExtremaBufferDouble(int n, double value) {
		this.buffer = new double[n];
		for (int i = 0; i < n; i++)
			this.buffer[i] = value;
		this.maxValue = value;
	}

	public void setMinMaxSign(int sign) 
	{
		this.sign = sign;
	}
	
	
	/**
	 * Adds a value to the local histogram, and update bounds if needed. 
	 * Then removes the last stored value, and update bounds if needed.
	 * @param value the value to add
	 */
	public void add(double value) {
		// add the new value, and remove the oldest one
		addValue(value);
		removeValue(this.buffer[this.bufferIndex]);
		
		// update local circular buffer
		this.buffer[this.bufferIndex] = value;
		this.bufferIndex = (++this.bufferIndex) % this.buffer.length;
	}
	
	private void addValue(double value) {
		// update max value
		if (value * sign > this.maxValue * sign) {
			updateNeeded = true;
		}
	}
	
	private void removeValue(double value) {
		// update max value if needed
		if (value == this.maxValue) {
			updateNeeded = true;
		}
	}
	
	private void updateMaxValue() {
		if (sign == 1)
		{
			// find the maximum value in the buffer
			this.maxValue = Integer.MIN_VALUE;
			for (int i = 0; i < buffer.length; i++) {
				this.maxValue = Math.max(this.maxValue, this.buffer[i]);
			}
		}
		else
		{
			// find the maximum value in the buffer
			this.maxValue = Integer.MAX_VALUE;
			for (int i = 0; i < buffer.length; i++) {
				this.maxValue = Math.min(this.maxValue, this.buffer[i]);
			}
		}
		
		updateNeeded = false;
	}
	
	/**
	 * Reset inner counts with default values (0 for MAX, 255 for MIN)
	 */
	public void clear() {
		if (this.sign == 1)
			this.fill(Double.MIN_VALUE);
		else
			this.fill(Double.MAX_VALUE);
	}
	
	/**
	 * Resets histogram by considering it is filled with the given value. 
	 * Update max and max accordingly.
	 */
	public void fill(double value) {
		// get buffer size
		int n = this.buffer.length;

		// Clear the circular buffer
		for (int i = 0; i < n; i++)
			buffer[i] = value;

		// update max and max values
		this.maxValue = value;
	}

	/**
	 * Returns the maximum value stored in this local histogram
	 * @return the maximum value in neighborhood
	 */
	public double getMax() {
		if (updateNeeded) {
			updateMaxValue();
		}
		
		return this.maxValue;
	}
}

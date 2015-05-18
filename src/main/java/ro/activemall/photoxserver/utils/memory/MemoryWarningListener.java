package ro.activemall.photoxserver.utils.memory;

public interface MemoryWarningListener {
	void memoryUsageLow(long usedMemory, long maxMemory);
}

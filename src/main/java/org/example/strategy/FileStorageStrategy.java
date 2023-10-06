package org.example.strategy;

public class FileStorageStrategy implements StorageStrategy {

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;
    FileBucket[] table;
    int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    long maxBucketSize;

    public FileStorageStrategy() {
        init();
    }

    private void init() {
        table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
        for (int i = 0; i < table.length; i++) {
            table[i] = new FileBucket();
        }
    }

    static int indexFor(int hash, int length) {
        return hash & length - 1;
    }

    final Entry getEntry(Long key) {
        if (size == 0) {
            return null;
        }
        int index = indexFor(key.hashCode(), table.length);

        for (Entry e = table[index].getEntry(); e != null; e = e.next) {
            if (e.key.equals(key)) {
                return e;
            }
        }
        return null;
    }

    void resize(int newCapacity) {
        FileBucket[] newTable = new FileBucket[newCapacity];
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = new FileBucket();
        }
        transfer(newTable);

        for (int i = 0; i < table.length; i++) {
            table[i].remove();
        }
        table = newTable;
    }

    void transfer(FileBucket[] newTable) {
        int newCapacity = newTable.length;
        maxBucketSize = 0;

        for (FileBucket fileBucket : table) {
            Entry entry = fileBucket.getEntry();

            while (entry != null) {
                Entry next = entry.next;
                int indexInNewTable = indexFor(entry.getKey().hashCode(), newCapacity);
                entry.next = newTable[indexInNewTable].getEntry();
                newTable[indexInNewTable].putEntry(entry);
                entry = next;
            }
            long currentBucketSize = fileBucket.getFileSize();
            if (currentBucketSize > maxBucketSize) {
                maxBucketSize = currentBucketSize;
            }
        }
    }

    @Override
    public String getValue(Long key) {
        Entry entry = getEntry(key);

        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }


    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (FileBucket tableElement : table) {
            for (Entry e = tableElement.getEntry(); e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void put(Long key, String value) {
        int hash = key.hashCode();
        int index = indexFor(hash, table.length);

        for (Entry e = table[index].getEntry(); e != null; e = e.next) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
    }

     void addEntry(int hash, Long key, String value, int index) {
        if((maxBucketSize>bucketSizeLimit)){
            resize(2*table.length);
            index =indexFor(key.hashCode(), table.length);
        }
        createEntry(hash,key,value,index);
    }

     void createEntry(int hash, Long key, String value, int index) {
        Entry e = table[index].getEntry();
        table[index].putEntry(new Entry(hash,key,value,e));
        size++;

        long currentBucketSize =table[index].getFileSize();
        if(currentBucketSize>maxBucketSize)
            maxBucketSize=currentBucketSize;
    }

    @Override
    public Long getKey(String value) {
        for (FileBucket fileBucket : table) {
            for (Entry e = fileBucket.getEntry(); e != null; e = e.next) {
                if (e.value.equals(value)) {
                    return e.getKey();
                }
            }
        }

        return null;
    }

}

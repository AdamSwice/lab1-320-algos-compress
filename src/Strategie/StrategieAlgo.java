package Strategie;

import java.io.File;
import java.util.List;

public interface StrategieAlgo {
    List<Integer> compress(String data);
    File decompress(File file);
}

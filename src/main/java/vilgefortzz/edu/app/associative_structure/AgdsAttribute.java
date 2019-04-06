package vilgefortzz.edu.app.associative_structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgdsAttribute {

    private List<AgdsValue> agdsValuesList = new ArrayList<>();
    private Map<String, List<AgdsValue>> agdsValues = new HashMap<>();

    @Override
    public String toString() {
        return "AgdsAttribute";
    }
}

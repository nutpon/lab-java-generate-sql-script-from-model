package lab.reflex;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyClass {
    public String privateField = "Private Value";
    public int publicField = 10;
}
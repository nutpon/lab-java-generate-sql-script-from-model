package lab.reflex;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyClass {

    @Column(name = "privateField")
    public String privateField = "Private Value";

    @Column(name = "publicField")
    public int publicField = 10;
}
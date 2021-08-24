package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok lombok = new HelloLombok();
        lombok.setAge(111);

        System.out.println("lombok.toString() = " + lombok);
    }
}

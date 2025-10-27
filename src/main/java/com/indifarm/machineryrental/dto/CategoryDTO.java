//package com.indifarm.machineryrental.dto;
//
//import lombok.Data;
//import java.util.List; // Import this
//
//@Data
//public class CategoryDTO {
//    private String name;
//    private String icon;
//    private String searchQuery;
//    private List<String> machines; // Add this line
//}
package com.indifarm.machineryrental.dto;

import lombok.Data;
import java.util.List; // Import this

@Data
public class CategoryDTO {
    private String name;
    private String icon;
    private String searchQuery;
    private List<String> machines; // Add this line
}
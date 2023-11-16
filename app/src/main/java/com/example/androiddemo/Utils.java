package com.example.androiddemo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    List listCopy(List list1){
        List list2=new ArrayList<Integer>();
         list2= (List) list1.stream().collect(Collectors.toList());
         return list2;
    }
}

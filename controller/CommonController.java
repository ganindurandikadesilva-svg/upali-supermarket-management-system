package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

public interface CommonController<T> {

    //abstract methods of commpon operations
    public ModelAndView UI(T t);
    public String saveRecordData(T t);
    public String updateRecordData(T t);
    public String deleteRecordData(T t);
    public List<T> getAllData();

}

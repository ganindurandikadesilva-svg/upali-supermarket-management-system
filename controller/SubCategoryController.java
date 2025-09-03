package lk.upalisupermarket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lk.upalisupermarket.dao.SubCategoryDao;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.SubCategory;


@RestController
public class SubCategoryController {



     @Autowired
    private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                            // the data for secuirity
    // privileged user kenek da kiyl blnna

    @Autowired // generate instance
    private SubCategoryDao subCategoryDao;

    // mapping to return subcategory data(URL-->"/subcategory/findall")
    @GetMapping(value = "/subcategory/findall", produces = "application/json")

    public List<SubCategory> findAllData() {
        return subCategoryDao.findAll(Sort.by(Direction.DESC,"id"));//get subcategory list in descending order
    }

    // mapping to return subcategory by given category data
    // (URL-->"/subcategory/bycategory/subcategory/bycategory?categoryid=1")
    @GetMapping(value = "/subcategory/bycategory", params = { "categoryid" }, produces = "application/json")
    public List<SubCategory> byCategory(@RequestParam("categoryid") Integer categoryid) {
        return subCategoryDao.byCategory(categoryid);
    }

    @PostMapping(value = "/subcategory/insert") // item is the frontend object(no primary key since its now getting
                                                // inserted)
    public String saveSubCategoryData(@RequestBody SubCategory subcategory) { // Type should be item and object sent
                                                                              // from frontend is item

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
      

        //check privilege by module name (in module table _> Sub_Category need to be there )
        Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(), "Sub_Category");
        // object

        // check authorization
        // Check whether the user has permission to insert

        if (usrPrivilege.getInsert_privilege()) {

            // check duplication
               SubCategory existSubCategoryByText = subCategoryDao.getSubNameByCategory(subcategory.getName(),subcategory.getItem_category_id().getId());
            if (existSubCategoryByText != null) {
                return "Save Unsuccessfull !\n Entered Sub Category Name : " + subcategory.getName() + " Already Exists..!";
            }

            try {
                // set auto added data

                // save operator
                subCategoryDao.save(subcategory);
                // dependencies

                return "OK";// frontend recieving ok

            } catch (Exception e) {

                return "Save Unsucessfull...!" + e.getMessage();

            }
        } else {
            return "Save UnsucessFull..!\nYou Have No Permission to Insert";
        }

    }

}

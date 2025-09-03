package lk.upalisupermarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import lk.upalisupermarket.dao.PrivilegeDao;
import lk.upalisupermarket.entity.Privilege;

@Controller
public class UserPrivilegeController {
    

    @Autowired
    private PrivilegeDao privilegeDao;//create an instance of Privilege Dao to access the data

    //define a function to get privilege by given user name and module name
    public Privilege getPrivilegeByUserModule(String username,String modulename){

        Privilege userprivilege=new Privilege();
        if (username.equals("Admin")) {
            
            // if its Admin set all the privileges true
            userprivilege.setSelect_privilege(true);//sets true 
            userprivilege.setInsert_privilege(true);  
            userprivilege.setDelete_privilege(true);
            userprivilege.setUpdate_privilege(true);
        } else {
            //methin ywn username pssword wlt privilege dao eke query eka excute krl ena output ek methn arrey ekt add wenw
            String userPrivilegeString=privilegeDao.getUserPrivilegeByUserModule(username, modulename);
            String[] userPrivilegeArray=userPrivilegeString.split(",");
            System.out.println(userPrivilegeString);
    
            userprivilege.setSelect_privilege(userPrivilegeArray[0].equals("1"));//sets true if equals to 1
            userprivilege.setInsert_privilege(userPrivilegeArray[1].equals("1"));  
            userprivilege.setDelete_privilege(userPrivilegeArray[2].equals("1"));
            userprivilege.setUpdate_privilege(userPrivilegeArray[3].equals("1"));
        }

      
        return  userprivilege;
    } 
}

package lk.upalisupermarket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import lk.upalisupermarket.entity.Privilege;

public interface PrivilegeDao extends JpaRepository<Privilege,Integer> {

    //Query to check db eke role id ekai module id ekai fronened eken ewn ekai same  wena record tiynwd blnw
    @Query(value = "Select p from Privilege p where p.role_id.id= ?1 and p.module_id.id= ?2" )//role_id.id kiyl dmme role object eken tiyn nisa
    //liynna puluwan jpa query witharai nativequery liynwnm true krnnn onee , uda eka jpa query ekk
    Privilege getPrivilegeRoleModule(Integer roleid, Integer moduleid);//its automatically adds the output of the query to this functions body

/* @Query(value = "SELECT * FROM privilege WHERE role_id = ?1 AND module_id = ?2", nativeQuery = true)
Privilege getPrivilegeRoleModule(Integer roleId, Integer moduleId); */


//query to get user privilege by given username and module name
    @Query(value="SELECT bit_or(p.select_privilege) as sel,bit_or(p.insert_privilege) as ins,bit_or(p.delete_privilege) as del,bit_or(p.update_privilege) as upd FROM upali_supermarket.privilege as p where p.module_id in (select m.id from upali_supermarket.module as m where m.name=?2) and p.role_id in (select uhr.role_id from upali_supermarket.user_has_role as uhr where uhr.user_id in (select u.id from upali_supermarket.user as u where u.username=?1));",
    nativeQuery=true)

String getUserPrivilegeByUserModule(String username,String modulename);
}

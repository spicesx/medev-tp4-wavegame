/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.centrale.infosi.pappl.logement.repositories;
import org.centrale.infosi.pappl.logement.items.Role;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
/**
 * Interface du repository des rôles
 * @author clesp
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Integer>,RoleRepositoryCustom{

    /**
     * 
     * @param roleId
     * @return
     */

    public Collection<Role> findByRoleId(@Param("roleId")Role roleId);

    /**
     *
     * @param roleNom
     * @return
     */
    public Collection<Role> findByRoleNom(@Param("roleNom")String roleNom);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.hibernate;

import java.sql.Types;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StandardBasicTypes;


public class FtuCustomOracleDialect extends Oracle10gDialect
{
    public FtuCustomOracleDialect()
    {
        registerHibernateType( Types.NVARCHAR, StandardBasicTypes.STRING.getName() );
        registerColumnType( Types.VARCHAR, "nvarchar($1)" );
        registerColumnType( Types.CLOB, "nclob" );
        registerColumnType( Types.NCLOB, "nclob" );
    }
}
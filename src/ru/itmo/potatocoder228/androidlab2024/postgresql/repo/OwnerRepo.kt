package postgresql.repo

import postgresql.dbManager.*
import postgresql.model.Owner
import postgresql.model.Query
import postgresql.model.QueryType.*

class OwnerRepo {
    private var manager: DBManager = DBManager()

    fun addOwner(owner: Owner): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(INSERT, owner)).getMessage()
        manager.closeConnection()
        return toRet
    }

    fun deleteOwner(owner: Owner): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(DELETE, owner)).getMessage()
        manager.closeConnection()
        return toRet
    }

    fun updateOwner(owner: Owner): String {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(UPDATE, owner)).getMessage()
        manager.closeConnection()
        return toRet
    }

    fun getOwners(): ArrayList<Owner> {
        manager.initConnection()
        val toRet = manager.queryExecutor(Query(SELECT_ALL)).getCollection()
        manager.closeConnection()
        return toRet
    }

}
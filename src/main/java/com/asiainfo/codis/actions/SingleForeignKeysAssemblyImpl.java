package com.asiainfo.codis.actions;

import com.asiainfo.codis.schema.CodisHash;

/**
 * Created by peng on 16/9/9.
 */
public class SingleForeignKeysAssemblyImpl extends Assembly {
    private String codisHashKey;

    @Override
    public boolean execute(CodisHash codisHash, String sourceTableName, String[] row) {
        super.codisHash = codisHash;
        super.sourceTableName = sourceTableName;
        super.row = row;

        if (codisHash.getForeignKeys().length != 1) {
            return false;

        }


        String codisHashKeyPostfix = getTargetValue(codisHash.getForeignKeys()[0]);
        if (codisHashKeyPostfix != null){
            codisHashKey = codisHash.getKeyPrefix() + ":" + codisHashKeyPostfix;
        }else {
            return false;
        }

        return true;
    }

}

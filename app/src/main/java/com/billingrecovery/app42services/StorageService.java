package com.billingrecovery.app42services;

import android.content.Context;

import com.billingrecovery.libs.AsyncApp42ServiceApi;
import com.billingrecovery.libs.Config;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Query;

import org.json.JSONObject;

public class StorageService {

    private AsyncApp42ServiceApi asyncService;

    public StorageService(Context context) {
        asyncService = AsyncApp42ServiceApi.instance(context);
    }

    public void insertDocs(String strCollectionName, JSONObject jsonToSave,
                           AsyncApp42ServiceApi.App42StorageServiceListener app42CallBack) {
        asyncService.insertJSONDoc(Config.dbName, strCollectionName, jsonToSave, app42CallBack);
    }

    /*public void findDocsByName(String checkValue) {
        asyncService.findDocByDocId(Config.dbName, Config.collectionProvider, checkValue, this);
    }*/

  /*  public void findDocsById(String strDocId, String strCollectionName, AsyncApp42ServiceApi.App42StorageServiceListener app42CallBack) {
        asyncService.findDocByDocId(Config.dbName, strCollectionName, strDocId, app42CallBack);
    }

    public void findDocsByIdApp42CallBack(String strDocId, String strCollectionName, App42CallBack app42CallBack) {
        asyncService.findDocByDocIdApp42CallBack(Config.dbName, strCollectionName, strDocId, app42CallBack);
    }*/

    public void findDocsByQuery(String strCollectionName, Query query,
                                App42CallBack app42CallBack) {
        asyncService.findDocumentByQuery(Config.dbName, strCollectionName, query, app42CallBack);
    }

    public void findDocsByQueryOrderBy(String strCollectionName, Query query, int max, int offset,
                                       String strKey, int iOrderFlag, App42CallBack app42CallBack) {
        asyncService.findDocumentByQueryPagingOrderBy(Config.dbName, strCollectionName, query, max,
                offset, strKey, iOrderFlag, app42CallBack);
    }
    /*public void findDocsByKeyValue(String strCollectionName, String strKey, String strValue,
                                   AsyncApp42ServiceApi.App42StorageServiceListener app42CallBack) {
        asyncService.findDocumentByKeyValue(Config.dbName, strCollectionName, strKey, strValue,
                app42CallBack);
    }*/

    /*public void updateDocs(JSONObject jsonToUpdate, String fieldName, String checkValue) {
        asyncService.updateDocByKeyValue(Config.dbName, Config.collectionProvider, fieldName, checkValue, jsonToUpdate, this);
    }*/

    public void updateDocs(JSONObject jsonToUpdate, String strDocId, String strCollectionName,
                           App42CallBack app42CallBack) {
        asyncService.updateDocPartByKeyValue(Config.dbName, strCollectionName, strDocId,
                jsonToUpdate, app42CallBack);
    }

    public void updateDocsService(JSONObject jsonToUpdate, String strDocId, String strCollectionName,
                                  App42CallBack app42CallBack) {
        asyncService.updateDocPartByKeyValueService(Config.dbName, strCollectionName, strDocId,
                jsonToUpdate, app42CallBack);
    }

    public void findAllDocs(String strCollectionName, App42CallBack app42CallBack) {
        asyncService.findAllDocuments(Config.dbName, strCollectionName, app42CallBack);
    }

    public void deleteDocById(String strCollectionName, String strDocId,
                              App42CallBack app42CallBack) {
        try {
            asyncService.deleteDocById(Config.dbName, strCollectionName, strDocId,
                    app42CallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

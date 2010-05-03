/*
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.telekommunisten.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.telekommunisten.Constants;

/**
 * SyncAdapter implementation for syncing sample SyncAdapter contacts to the
 * platform ContactOperations provider.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";

    private final AccountManager mAccountManager;
    private final Context mContext;

    private Date mLastUpdated;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        mAccountManager = AccountManager.get(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
        ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSYNC");
        
        Cursor c = getContext().getContentResolver().query(RawContacts.CONTENT_URI,
                new String[]{RawContacts._ID}, null, null, null);
        Log.d(TAG, "da "+c.getCount());
        Cursor c2 = getContext().getContentResolver().query(Contacts.CONTENT_URI,
                new String[]{Contacts._ID}, null, null, null);
        Log.d(TAG, "da all "+c.getCount());
        
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (int i = 0; i < 7; i++) {
                int rawContactInsertIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                        .withValue(RawContacts.ACCOUNT_TYPE, account.type)
                        .withValue(RawContacts.ACCOUNT_NAME, account.name)
                        .build());
                
                ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, "DAO"+i)
                        .build());
                ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, Constants.MIMETYPE)
                        .withValue(Data.DATA1, i)
                        .withValue(Data.DATA2, "lk")
                        .withValue(Data.DATA3, "lööö")
                        .build());
            }
            getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
}

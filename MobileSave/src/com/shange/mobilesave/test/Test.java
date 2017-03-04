package com.shange.mobilesave.test;

import java.io.File;
import java.util.ArrayList;

import com.shange.mobilesave.db.dao.BlackNumberDao;
import com.shange.mobilesave.db.domain.BlackNumberInfo;

import android.os.Environment;
import android.test.AndroidTestCase;

public class Test extends AndroidTestCase{

	public void insert(){
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		dao.insert("110", "1");
		
	// /storage/emulated/0/blacknumber.db

	}
	public void delete(){
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		dao.delete("110");
		
	}
	public void update(){
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		dao.update("110","2");
	}
	public void findAll(){
		BlackNumberDao dao = BlackNumberDao.getInstance(getContext());
		ArrayList<BlackNumberInfo> arrayList = dao.findAll();
	}
	
}

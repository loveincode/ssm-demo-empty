package com.kedacom.smu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduledExecutorBox<T extends Runnable> implements Runnable {
	private Class<T> mClass;

	public ScheduledExecutorBox(Class<T> cls) {
		mClass = cls;
	}

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void run() {
		try {
			String st = format.format(new Date());

			System.out.println(this.getClass().getSimpleName() + " :+++++ START," + this + "  " + st);

			T cc = mClass.newInstance();

			System.out.println(cc.getClass().getSimpleName() + " :+++++ START," + this + "  " + st);

			Thread t = new Thread(cc);
			t.start();
			t.join();

			cc = null;
			t = null;
			// System.gc();

			st = format.format(new Date());
			System.out.println(this.getClass().getSimpleName() + " :+++++ END," + this + "  " + st + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package com.example.feiya.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int port = 30000;
    private int timeout = 5000;
    private Socket socket;
    private boolean IsConnceted = false;
    private boolean IsShift = false;
    private boolean IsStarted = false;

    private SurfaceView surfaceView;
    private MySurfaceView mySurfaceView;
    private TabLayout tabLayout;
    private ProgressDialog progressDialog;

    private Process process;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.ic_filter_tilt_shift_black_24dp);
        setSupportActionBar(toolbar);

        //tabLayout=(TabLayout)findViewById(R.id.tablayout);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceiew);

        mContext = getApplicationContext();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.rgb(238, 238, 238));
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        process = new Process(mContext, surfaceView);
        // process.startAM();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (IsConnceted) {
            //menu.clear();
            MenuItem connectedItem = menu.findItem(R.id.connected);
            MenuItem connectItem = menu.findItem(R.id.connect);
            connectItem.setVisible(false);
            connectedItem.setVisible(true);
            //this.getMenuInflater().inflate(R.menu.menu_main,menu);
        } else {
            MenuItem connectedItem = menu.findItem(R.id.connected);
            MenuItem connectItem = menu.findItem(R.id.connect);
            connectItem.setVisible(true);
            connectedItem.setVisible(false);
        }
        if (IsStarted) {
            MenuItem startdItem = menu.findItem(R.id.start);
            MenuItem pauseItem = menu.findItem(R.id.pause);
            startdItem.setVisible(false);
            pauseItem.setVisible(true);
        } else {
            MenuItem startdItem = menu.findItem(R.id.start);
            MenuItem pauseItem = menu.findItem(R.id.pause);
            startdItem.setVisible(true);
            pauseItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    private Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast;
            switch (msg.what){
                case 0x00:
                    progressDialog.dismiss();
                    IsConnceted = true;
                    invalidateOptionsMenu();
                    break;
                case 0x11:
                    toast = Toast.makeText(getApplicationContext(), "连接服务器端超时==", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    progressDialog.dismiss();
                    break;
                case 0x22:
                    toast = Toast.makeText(getApplicationContext(), "连接失败,服务器端好像没打开==", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    progressDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.settings) {
            return true;
        } else if (id == R.id.connect) {

            if (WiFiStatus.isWifiConnected(MainActivity.this)) {
                progressDialog=ProgressDialog.show(MainActivity.this,"","connecting...");
                progressDialog.setCancelable(true);
               Thread thread=new Thread(new ConnectThread(handler,MainActivity.this));
                thread.start();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "wifi还没有打开啊喂！", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (id == R.id.connected) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (IsStarted) {
                    IsStarted = false;
                }
                process.stop();
                IsConnceted = false;
                invalidateOptionsMenu();
            }
        } else if (id == R.id.start) {
            if (IsConnceted) {
                try {
                    process.start(surfaceView, socket);
                    if (surfaceView == null) {
                        Log.e("aactivitysurface", "null");
                    } else {
                        Log.e("aactivitysurface", "notnull");
                    }
                    IsStarted = true;
                    invalidateOptionsMenu();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "还未连接呀", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (id == R.id.pause) {
            process.stop();
            IsStarted = false;
            invalidateOptionsMenu();
        } else if (id == R.id.change) {
            if (IsStarted) {
                if (IsShift) {
                    process.setWitchshift();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "请先选中形变菜单项", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "请先开始", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        switch (id) {
            case R.id.am:
                process.startAM();
                IsShift = false;
                toolbar.setLogo(R.drawable.ic_menu_am_white);
                break;
            case R.id.phase:
                process.startPhase();
                IsShift = false;
                toolbar.setLogo(R.drawable.ic_menu_phase_white);
                break;
            case R.id.shift:
                process.startShift();
                IsShift = true;
                toolbar.setLogo(R.drawable.ic_menu_shift_white);
                break;
            case R.id.signal:
                toolbar.setLogo(R.drawable.ic_menu_signal_white);
                IsShift = false;
                process.startSignal();
                break;
            case R.id.about_us:
                surfaceView.setZOrderOnTop(false);
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                IsShift = false;
                break;
            case R.id.exit_sys:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("你确定退出吗？");
                builder.setIcon(R.drawable.ic_menu_exit);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        process.stop();
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        process.stop();
        super.onDestroy();
    }
}

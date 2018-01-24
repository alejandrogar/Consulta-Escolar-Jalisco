package mx.gob.jalisco.edu.consultaescolar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.gob.jalisco.edu.consultaescolar.fragments.tabs.NotificationsFragment;
import mx.gob.jalisco.edu.consultaescolar.fragments.tabs.SchoolFragment;
import mx.gob.jalisco.edu.consultaescolar.fragments.tabs.ServicesFragment;
import mx.gob.jalisco.edu.consultaescolar.utils.NetworkUtils;
import mx.gob.jalisco.edu.consultaescolar.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_action_social_school_white,
            R.drawable.ic_action_icono_escuela_white,
            R.drawable.ic_action_social_notifications_white
    };
    private int[] unActivetabIcons = {
            R.drawable.ic_action_social_school_white,
            R.drawable.ic_action_icono_escuela_white,
            R.drawable.ic_action_social_notifications_white
    };

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    public static final String PREF_USER_APP_UPDATED = "user_app_updated";

    boolean isUserFirstTime;


    ServicesFragment servicesFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkUtils internet = new NetworkUtils(this);
        if(internet.isConnectingToInternet()){
            setContentView(R.layout.activity_main);

            TextView titleApp = (TextView) findViewById(R.id.titleApp);
            titleApp.setText("Consulta Escolar");

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("");
            }

            final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

            if(Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_APP_UPDATED, "true"))){
                //FirebaseMessaging.getInstance().subscribeToTopic("all_users");
                Log.i("SUBSCIBE_TO_TOPIC", "true");
                Utils.saveSharedSetting(MainActivity.this, PREF_USER_APP_UPDATED, "updated");
            }


        /*
        if (isUserFirstTime) {

        }
        */
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.icon).setBackgroundTintList(getResources().getColorStateList(R.color.white));
                    viewPager.setCurrentItem(tab.getPosition());

                    Log.d("TAB SELECTED", tab.getPosition()+"");
                    if(tab.getPosition() == 1){
                        servicesFragment.reloadItems();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tabLayout.getTabAt(tab.getPosition()).getCustomView().findViewById(R.id.icon).setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            setupTabIcons();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setMessage(R.string.alert_no_internet)
                    .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            builder.create().show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=mx.gob.jalisco.edu.consultaescolar");
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupTabIcons() {
        View view1 = getLayoutInflater().inflate(R.layout.item_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(tabIcons[0]);
        tabLayout.getTabAt(0).setCustomView(view1);
        view1.findViewById(R.id.icon).setBackgroundTintList(getResources().getColorStateList(R.color.white));

        View view2 = getLayoutInflater().inflate(R.layout.item_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(tabIcons[1]);
        tabLayout.getTabAt(1).setCustomView(view2);
        view2.findViewById(R.id.icon).setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));

/*
        View view3 = getLayoutInflater().inflate(R.layout.item_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(tabIcons[2]);
        tabLayout.getTabAt(2).setCustomView(view3);
        view3.findViewById(R.id.icon).setBackgroundTintList(getResources().getColorStateList(R.color.black_overlay));*/
    }

    private void setupViewPager(ViewPager viewPager) {
        servicesFragment = new ServicesFragment();

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(servicesFragment, "ALUMNOS");
        adapter.addFragment(new SchoolFragment(), "ESCUELA");
        //adapter.addFragment(new NotificationsFragment(), "NOTIFICACIONES");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }
    }
}

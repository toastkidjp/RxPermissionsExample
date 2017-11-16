package jp.toastkid.rxpermissionsexample

import android.Manifest
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.Snackbar.LENGTH_SHORT
import android.support.v7.app.AppCompatActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun save() {
        disposables.add(
                rxPermissions
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { granted ->
                                    if (granted) {
                                        FileActions.save(this, "sample.txt")
                                    } else {
                                        Snackbar.make(
                                                container,
                                                "It wasn't granted WRITE_EXTERNAL_STORAGE.",
                                                LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                { Timber.e(it) }
                        )
        )
    }

    private fun load() {
        disposables.add(
                rxPermissions
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { granted ->
                                    if (granted) {
                                        message.text = FileActions.load(this, "sample.txt")
                                    } else {
                                        Snackbar.make(
                                                container,
                                                "It wasn't granted READ_EXTERNAL_STORAGE.",
                                                LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                { Timber.e(it) }
                        )
        )
    }

    private lateinit var rxPermissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxPermissions = RxPermissions(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        save.setOnClickListener { save() }
        load.setOnClickListener { load() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

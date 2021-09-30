package com.pureblacksoft.creepyone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.databinding.ActivityPrefBinding
import com.pureblacksoft.creepyone.fragment.PrefFragment
import kotlinx.android.synthetic.main.activity_pref.view.*

class PrefActivity : AppCompatActivity()
{
    companion object {
        //region Fragment Id
        const val ID_FRAGMENT_PREF = 0
        const val ID_FRAGMENT_INFO = 1
        //endregion

        var currentFragmentId = ID_FRAGMENT_PREF
    }

    private lateinit var rootPA: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootPA = ActivityPrefBinding.inflate(layoutInflater).root
        setContentView(rootPA)

        //region Toolbar
        rootPA.ivBackPTB.setOnClickListener {
            onBackPressed()
        }

        val packInfo = this.packageManager.getPackageInfo(this.packageName, 0)
        rootPA.tvVersionPTB.text = getString(R.string.App_Version, packInfo.versionName)
        //endregion

        //region Comit PrefFragment
        comitFragment(PrefFragment())
        //endregion
    }

    fun comitFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerPA, fragment).commit()
    }

    override fun onBackPressed() {
        if (currentFragmentId != ID_FRAGMENT_PREF) {
            comitFragment(PrefFragment())
        }
        else {
            finish()
        }
    }
}

//PureBlack Software / Murat BIYIK
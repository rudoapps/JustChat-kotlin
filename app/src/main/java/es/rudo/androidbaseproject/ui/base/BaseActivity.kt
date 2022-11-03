package es.rudo.androidbaseproject.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import es.rudo.androidbaseproject.BR
import es.rudo.androidbaseproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM: BaseViewModel, VB: ViewDataBinding> : AppCompatActivity() {

    lateinit var viewModel: VM
    lateinit var binding: VB

    protected abstract val layoutId: Int
    private var progressContainer: ViewGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

        var activityClass: Class<in BaseActivity<VM, VB>> = javaClass
        while (activityClass.genericSuperclass !is ParameterizedType) {
            activityClass = activityClass.superclass as Class<in BaseActivity<VM, VB>>
        }

        val type = activityClass.genericSuperclass as ParameterizedType
        viewModel = ViewModelProvider(this)[type.actualTypeArguments[0] as Class<VM>]

        binding.setVariable(BR.activity, this)
        binding.setVariable(BR.viewModel, viewModel)

        setContentView(binding.root)
        viewModel.initData(Bundle())

        setProgressContainer()
        setUpViews()
    }

    protected abstract fun setUpViews()

    fun <T> background(callback: ((T?) -> Unit)? = null, process: () -> T?) =
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                process()
            }

            callback?.let {
                withContext(Dispatchers.Main) {
                    callback(result)
                }
            }
        }

    private fun setProgressContainer() {
        progressContainer = findViewById(R.id.progress_container)
    }

    /**
     * Manage progress container
     */

    protected fun showProgressContainer() {
        progressContainer?.let {
            it.visibility = View.VISIBLE
        }
    }

    protected fun hideProgressContainer() {
        progressContainer?.let {
            it.visibility = View.GONE
        }
    }
}

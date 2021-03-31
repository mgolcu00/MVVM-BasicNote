package com.mertgolcu.basicnote.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.showSnackBar
import com.mertgolcu.basicnote.utils.LoadingDialog
import kotlinx.coroutines.flow.collect


abstract class BaseFragment<B : ViewDataBinding, M : BaseViewModel> constructor(
    @LayoutRes private val layoutId: Int
) : Fragment(layoutId) {

    //private val viewModel: BaseViewModel by viewModels()

    private var loadingDialog: LoadingDialog? = null


    abstract val viewModel: M

    // Binding
    lateinit var binding: B

    var rootView: View? = null
        private set

    var isViewCreated = false
        private set


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isViewCreated) {
            return rootView
        }
        loadingDialog = LoadingDialog(requireContext())
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            (viewModel as BaseViewModel).baseEvent.collect {
                onViewEvent(it)
            }
        }
    }

    private fun onViewEvent(event: BaseViewEvent) {
        when (event) {
            is BaseViewEvent.NavigateTo -> {
                findNavController().navigate(event.directions)
            }
            is BaseViewEvent.ShowErrorOrSuccessMessage -> {
                when (event.type) {
                    EventType.SUCCESS -> {
                        event.msg.showSnackBar(
                            requireView(),
                            R.color.success_green
                        )
                    }
                    EventType.ERROR -> event.msg.showSnackBar(requireView())
                }
            }
            is BaseViewEvent.ShowLoadingDialog ->
                if (event.show)
                    loadingDialog?.showDialog()
                else
                    loadingDialog?.dismissDialog()
            is BaseViewEvent.NavigateBack ->
                if (event.destination == null)
                    findNavController().popBackStack()
                else
                    findNavController().popBackStack(event.destination, false)
        }
    }
}

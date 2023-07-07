package com.example.notescleanarchitecture.presentation

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

open class BaseFragment<VB : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB? get() = _binding
    protected val navController: NavController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAlert(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String? = null,
        onPositiveButtonClick: ((dialog: DialogInterface) -> Unit)? = null,
        onNegativeButtonClick: ((dialog: DialogInterface) -> Unit)? = null,
        onDismiss: (() -> Unit)? = null,
    ) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositiveButtonClick?.invoke(dialog)
            }
        negativeButtonText?.let {
            builder.setNegativeButton(it) { dialog, _ ->
                onNegativeButtonClick?.invoke(dialog)
            }
        }
        builder.setOnDismissListener {
            onDismiss?.invoke()
        }
        builder.create().show()
    }
}
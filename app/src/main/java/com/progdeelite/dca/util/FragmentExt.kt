package com.progdeelite.dca.util

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

/** NAVEGA PARA O DESTINO INDICADO ATRAVES DO ID DO NAGIVATION GRAPH */
fun Fragment.navTo(@IdRes dest: Int) = findNavController().navigate(dest)
fun Fragment.navTo(directions: NavDirections) = findNavController().navigate(directions)
fun Fragment.navTo(@IdRes dest: Int, args: Bundle) = findNavController().navigate(dest, args)
fun Fragment.navBack() = findNavController().navigateUp()

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA NA TELA DO CELULAR */
fun Fragment.toast(msg: String) = Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA POREM RETANGULAR. TAMBÉM PODE DAR A OPCÃO DE ADICIONAR ACÕES */
fun Fragment.snake(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

/** DISPARA UMA ACTIVITY PARTINDO DE UM FRAGMENT */
fun Fragment.startActivity(clazz: Class<*>, name: String = "", args: Bundle = Bundle()) {
    val intent = Intent(requireContext(), clazz).apply {
        if (!(name.isNotEmpty() && args.isEmpty)) {
            putExtra(name, args)
        }
    }
    requireActivity().startActivity(intent)
}

/** VERIFICA SE A PERMISSÃO FOI CONCEDIDA */
fun Fragment.hasPermission(permission: String): Boolean {
    val permissionCheckResult = ContextCompat.checkSelfPermission(requireContext(), permission)
    return PackageManager.PERMISSION_GRANTED == permissionCheckResult
}

/** VERIFICA SE DEVE SOLICITAR AS PERMISSÕES NOVAMENTE */
fun Fragment.shouldRequestPermission(permissions: Array<String>): Boolean {
    val grantedPermissions = mutableListOf<Boolean>()
    permissions.forEach { permission ->
        grantedPermissions.add(hasPermission(permission))
    }
    return grantedPermissions.any { granted -> !granted }
}

/** ESCONDE O TECLADO */
fun Fragment.hideKeyboard(view: View? = activity?.window?.decorView?.rootView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view?.hideKeyboard(view)
    } else {
        inputMethodManager()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)
    }
}

/** EXIBE O TECLADO */
fun Fragment.showKeyboard(view: View? = activity?.currentFocus) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view?.showKeyboard(view)
    } else {
        view?.let {
            it.postDelayed({
                it.requestFocus()
                inputMethodManager()?.showSoftInput(it, SHOW_FORCED)
            }, 100)
        }
    }
}

fun Fragment.inputMethodManager() =
    context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager



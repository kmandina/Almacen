package com.auu.hunterblade.almacen.ui.fragments.notifications

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.auu.hunterblade.almacen.R
import com.auu.hunterblade.almacen.databinding.FragmentNotificationsBinding
import com.auu.hunterblade.almacen.ui.fragments.home.HomeFragmentDirections

class NotificationsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        context ?: return binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cv1 = view.findViewById<CardView>(R.id.cv1)

        cv1.setOnClickListener{

            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.action_feedback)
                .setMessage(R.string.send_feedback_ask)
                .setNegativeButton(
                    R.string.cancel
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setPositiveButton(R.string.accept) { dialogInterface, _ ->
                    composeEmail(arrayOf(getString(R.string.email_address)), getString(
                        R.string.action_feedback))
                    dialogInterface.dismiss()
                }.show()
        }

    }

    fun composeEmail(addresses: Array<String?>?, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }
}

package com.abhinav.streamchatapp.ui.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.abhinav.streamchatapp.databinding.FragmentChannelBinding
import com.abhinav.streamchatapp.ui.BindingFragment
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory

class ChannelFragment : BindingFragment<FragmentChannelBinding>() {

    private val viewModel: ChannelViewModel by activityViewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentChannelBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = viewModel.getUser()
        if(user == null) {
            findNavController().popBackStack()
            return
        }

        val factory = ChannelListViewModelFactory(
            filter = Filters.and(Filters.eq("type", "messaging")),
            sort = ChannelListViewModel.DEFAULT_SORT,
            limit = 30
        )
        val chanelViewModel: ChannelListViewModel by viewModels { factory }
        val chanelHeaderViewModel: ChannelListHeaderViewModel by viewModels()
        chanelViewModel.bindView(binding.channelListView, viewLifecycleOwner)
        chanelHeaderViewModel.bindView(binding.channelListHeaderView, viewLifecycleOwner)

        binding.channelListHeaderView.setOnUserAvatarClickListener {
            viewModel.logout()
            findNavController().popBackStack()
        }
    }
}
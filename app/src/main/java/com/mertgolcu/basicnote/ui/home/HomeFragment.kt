package com.mertgolcu.basicnote.ui.home

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.databinding.FragmentHomeBinding
import com.mertgolcu.basicnote.ext.hideKeyboard
import com.mertgolcu.basicnote.ext.showSnackBar
import com.mertgolcu.basicnote.ui.home.adapter.NoteAdapter
import com.mertgolcu.basicnote.ui.home.adapter.NoteLoadStateAdapter
import com.mertgolcu.basicnote.utils.SwipeHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home),
    NoteAdapter.OnItemClickListener {

    override val viewModel: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NoteAdapter(this)
        binding.apply {

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(false)
            DividerItemDecoration(
                requireActivity(),
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            ).apply {
                recyclerView.addItemDecoration(this)
            }
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NoteLoadStateAdapter(),
                footer = NoteLoadStateAdapter()
            )


            titleBarHome.textViewSearchCancel.setOnClickListener {
                if (titleBarHome.editTextSearchBar.text.toString().isNotBlank())
                    titleBarHome.editTextSearchBar.setText("")
                titleBarHome.editTextSearchBar.clearFocus()
                requireView().hideKeyboard()
            }
            titleBarHome.imageButtonProfile.setOnClickListener {
                viewModel.goProfileFragment()
            }
            titleBarHome.editTextSearchBar.onFocusChangeListener =
                View.OnFocusChangeListener { _, isFocused ->
                    onSearchUI(binding, isFocused)
                }

            titleBarHome.editTextSearchBar.addTextChangedListener {
                viewModel.searchNotes(it.toString())
            }

            object : SwipeHelper(requireActivity(), recyclerView, false) {
                override fun instantiateUnderlayButton(
                    viewHolder: RecyclerView.ViewHolder?,
                    underlayButtons: List<UnderlayButton?>?
                ) {

                    //edit button
                    (underlayButtons as MutableList<UnderlayButton>).add(
                        UnderlayButton(
                            "",
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_delete
                            )!!,
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.delete_button_background
                            ),
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.delete_button_background
                            )
                        ) { id ->
                            viewModel.deleteNote(adapter.noteWithIndex(id))
                        }
                    )
                    //edit button
                    underlayButtons.add(
                        UnderlayButton(
                            "",
                            ContextCompat.getDrawable(
                                requireActivity(),
                                R.drawable.ic_edit
                            )!!,
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.edit_button_background
                            ),
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.edit_button_background
                            )
                        ) { id ->
                            viewModel.onClickEdit(adapter.noteWithIndex(id))
                        }
                    )
                }
            }
            buttonAddNote.setOnClickListener {
                titleBarHome.editTextSearchBar.clearFocus()
                requireView().hideKeyboard()
                viewModel.onClickAdd()
            }
        }


        viewModel.notes.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, (it as PagingData<Note>))
        }


        adapter.addLoadStateListener { loadState ->
            binding.apply {
                textViewHomeEmpty.isVisible = loadState.source.refresh is LoadState.Error
                recyclerView.isVisible = !textViewHomeEmpty.isVisible

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    textViewHomeEmpty.isVisible = true
                    recyclerView.isVisible = false
                }
            }

        }

        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>("result")
            ?.observe(viewLifecycleOwner) {
                if (it == "success") {
                    adapter.refresh()
                    getString(R.string.note_deleted).showSnackBar(
                        requireView(),
                        R.color.success_green
                    )
                }
            }
    }

    override fun onItemClick(note: Note) {
        viewModel.onClickNote(note)
    }

    private fun onSearchUI(binding: FragmentHomeBinding, isSearch: Boolean) {
        binding.titleBarHome.apply {
            textViewSearchCancel.isVisible = isSearch
            imageButtonProfile.isVisible = !isSearch
            val id = if (isSearch) textViewSearchCancel.id else imageButtonProfile.id
            editTextSearchBar.updateLayoutParams<ConstraintLayout.LayoutParams>
            {
                endToStart = id
            }
        }
    }
}
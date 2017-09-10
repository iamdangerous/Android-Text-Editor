package com.example.rkrde.awesometexteditor.uiView

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.rkrde.awesometexteditor.modal.FileModal
import com.example.rkrde.awesometexteditor.R
import com.example.rkrde.awesometexteditor.modal.Notes

/**
 * Created by rkrde on 09-09-2017.
 */

class EditorView : FrameLayout {
    private var myContext: Context

    lateinit var ll: LinearLayout

    val etList = arrayListOf<AppCompatEditText>()
    val imgList = arrayListOf<AppCompatImageView>()
    val notesList = arrayListOf<Notes>()


    fun showNoteFromDb(notes: List<Notes>) {
        clearAllList()
        notesList.addAll(notes)

        notesList.forEach { x ->
            when (x.type) {
                Notes.TYPE_TEXT -> addEditextFromDb(x)
                Notes.TYPE_IMAGE-> addImageFromDb(x)
            }
        }

    }

    fun clearAllList() {
        etList.clear()
        imgList.clear()
        notesList.clear()
        ll.removeAllViews()
    }

    fun addEditextFromDb(note: Notes) {

        val et = AppCompatEditText(context)
        et.layoutParams = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll.addView(et)
        et.gravity = Gravity.TOP
        et.setText(note.text)

        if (etList.size > 1)
            etList.get(etList.size - 1).clearFocus()

        et.requestFocus()
        etList.add(et)

    }

    fun addImageFromDb(notes: Notes) {

    }

    fun addBitmap(bitmap: Bitmap) {
        val imageView = AppCompatImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.adjustViewBounds = false

        var focusedEt = 0

        var size: Int = ll.childCount
        val indexes = 0..size

        for (i in indexes) {
            if (ll.getChildAt(i) is AppCompatEditText) {
                ll.getChildAt(i).layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                if (ll.getChildAt(i).isFocused) {
                    focusedEt = i
                }
            }

        }

        val indexOfImageView = focusedEt + 1
        ll.addView(imageView, indexOfImageView)
        imageView.setImageBitmap(bitmap)
        imgList.add(imageView)

        val index = ll.childCount
        notesList.add(Notes(index, Notes.TYPE_IMAGE))

        if (isLastEtFocused())
            addNewEditText()
    }

    fun isLastEtFocused(): Boolean {
        return etList.get(etList.size - 1).isFocused
    }


    fun addNewEditText() {

        val et = AppCompatEditText(context)
//        et.layoutParams = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,resources.getDimensionPixelOffset(R.dimen.et_height))
        et.layoutParams = FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll.addView(et)
        et.gravity = Gravity.TOP
        et.setHint(etList.size.toString())

        if (etList.size > 1)
            etList.get(etList.size - 1).clearFocus()

        et.requestFocus()
        etList.add(et)
        val index = ll.childCount
        notesList.add(Notes(index, Notes.TYPE_TEXT))
    }

    fun addImage(fileModal: FileModal) {

        val resultUri = fileModal.uri
        val absolute_path = resultUri?.path

        val imageView = AppCompatImageView(context)
        imageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        imageView.adjustViewBounds = true

        etList.forEach { x -> x.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT) }

        ll.addView(imageView)
        Glide.with(context)
                .load(absolute_path)
                .into(imageView)

    }


    constructor(context: Context) : super(context) {
        this.myContext = context
        inflateLayout()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.myContext = context
        inflateLayout()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.myContext = context
        inflateLayout()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        this.myContext = context
        inflateLayout()
    }


    fun inflateLayout() {
        val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = mInflater.inflate(R.layout.view_editor, this, true)
        val et_1: AppCompatEditText = view.findViewById(R.id.et_1)
        etList.add(et_1)
        ll = view.findViewById(R.id.ll)

        notesList.add(Notes(Notes.TYPE_TEXT, 0))

    }
}
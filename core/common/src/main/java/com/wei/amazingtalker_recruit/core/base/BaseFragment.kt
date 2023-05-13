package com.wei.amazingtalker_recruit.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * BaseFragment 是一個抽象類，它封裝了共享的 Fragment 邏輯和 ViewBinding。
 * B 是一個 ViewBinding 的型別參數。
 *
 * BaseFragment 提供了一種方便的方式來處理 ViewBinding，並在 onCreateView 中自動創建和清理。
 * 並且還提供了三個抽象方法 (setupViews, setupObservers, init)，讓子類可以根據自己的需要實現。
 */
abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    /**
     * 用於通過 LayoutInflater、ViewGroup 和 Boolean 參數來創建 ViewBinding 的抽象方法。
     */
    abstract val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * onViewCreated 方法中，調用了三個抽象方法：setupViews，setupObservers 和 init
     * 分別用於設置視圖，設置觀察者和初始化。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        init()
    }

    /**
     * 用於設置視圖的抽象方法，子類必須實現。
     */
    abstract fun setupViews()

    /**
     * 用於設置觀察者的抽象方法，子類必須實現。
     */
    abstract fun setupObservers()

    /**
     * 用於進行初始化的抽象方法，子類必須實現。
     */
    abstract fun init()

    /**
     * 在 onDestroyView 方法中，將 _binding 設置為 null，以避免內存泄露。
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

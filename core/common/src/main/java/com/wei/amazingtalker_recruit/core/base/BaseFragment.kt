package com.wei.amazingtalker_recruit.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.wei.amazingtalker_recruit.core.extensions.observeEvent
import com.wei.amazingtalker_recruit.core.models.Event

/**
 * BaseFragment 是一個抽象類，它封裝了共享的 Fragment 邏輯和 ViewBinding。
 * B 是一個 ViewBinding 的型別參數。
 * VM 是一個 BaseViewModel 的型別參數。
 *
 * BaseFragment 提供了一種方便的方式來處理 ViewBinding，並在 onCreateView 中自動創建和清理。
 * 並且還提供了五個抽象方法 (setupViews, addOnClickListener, setupObservers, init, handleEvent)，讓子類可以根據自己的需要實現。
 */
abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel> : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!

    /**
     * 用於通過 LayoutInflater、ViewGroup 和 Boolean 參數來創建 ViewBinding 的抽象方法。
     */
    abstract val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B
    /**
     * 用於監聽 UI Event 和 UI State 的抽象 ViewModel，必須由子類實現。
     * 在這裡，ViewModel 被用來監聽 UI Event 和 UI State，並提供相應的方法來對這些事件和狀態進行處理。
     * 需要注意的是，這裡的 ViewModel 必須是 VM 類型的，並且需要由子類來實現這個 ViewModel。
     */
    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    /**
     * onViewCreated 方法中，調用了三個抽象方法：setupViews，addOnClickListener，setupObservers，init 和 handleEvent
     * 分別用於設置視圖，設置視圖點擊事件，設置觀察者和初始化。
     * handleEvent() 處理來至 ViewModel Event Flow 對應邏輯。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 調用 Fragment 的父類方法，這是必須的，以保證視圖的生命周期正常運行
        super.onViewCreated(view, savedInstanceState)

        // 調用子類實現的 setupViews() 方法，用於設置視圖的各種屬性
        binding.setupViews()

        // 調用子類實現的 addOnClickListener() 方法，用於為視圖設置 OnClickListener
        binding.addOnClickListener()

        // 調用子類實現的 setupObservers() 方法，設置 UI狀態 觀察者，以便根據 LiveData 或其他可觀察對象的變化來更新 UI
        // TODO warning 待 MVI 架構，遷移至 setupStateObservers()
        viewLifecycleOwner.lifecycleScope.setupObservers()

        // 設置 Events 觀察者，當 Events 發生變化時，調用 handleEvent() 方法來處理這些事件
        viewModel.events.observeEvent(viewLifecycleOwner) { event ->
            handleEvent(event)
        }

        // 調用子類實現的 initData() 方法，用於進行資料的初始化工作，這可能包括從資料庫獲取資料，或者從網路請求資料等
        initData()
    }

    /**
     * 用於設置視圖的抽象方法，子類必須實現。
     */
    abstract fun B.setupViews()

    /**
     * 用於設置視圖 OnClickListener 的抽象方法，子類必須實現。
     */
    abstract fun B.addOnClickListener()

    /**
     * 用於設置觀察者的抽象方法，子類必須實現。
     * warning: 待 MVI 架構遷移後，棄用
     */
    abstract fun LifecycleCoroutineScope.setupObservers()

    /**
     * 用於處理來至 ViewModel Event Flow 的事件，子類必須實現。
     * 子類應該根據這些事件來更新 UI 或執行其他相應的操作。
     */
    abstract fun handleEvent(event: Event)

    /**
     * 用於進行初始化資料的抽象方法，子類必須實現。
     */
    abstract fun initData()

    /**
     * 在 onDestroyView 方法中，將 _binding 設置為 null，以避免內存泄露。
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

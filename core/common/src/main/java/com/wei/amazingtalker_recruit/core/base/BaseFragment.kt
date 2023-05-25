package com.wei.amazingtalker_recruit.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.wei.amazingtalker_recruit.core.models.Action
import com.wei.amazingtalker_recruit.core.models.State
import kotlinx.coroutines.flow.StateFlow

/**
 *
 * UI 事件決策樹
 * 下圖顯示了一個決策樹，用於查找處理特定事件用例的最佳方法。
 *
 *                                                      ┌───────┐
 *                                                      │ Start │
 *                                                      └───┬───┘
 *                                                          ↓
 *                                       ┌───────────────────────────────────┐
 *                                       │ Where is event originated?        │
 *                                       └──────┬─────────────────────┬──────┘
 *                                              ↓                     ↓
 *                                              UI                  ViewModel
 *                                              │                     │
 *                           ┌─────────────────────────┐      ┌───────────────┐
 *                           │ When the event requires │      │ Update the UI │
 *                           │ ...                     │      │ State         │
 *                           └─┬─────────────────────┬─┘      └───────────────┘
 *                             ↓                     ↓
 *                        Business logic      UI behavior logic
 *                             │                     │
 *     ┌─────────────────────────────────┐   ┌──────────────────────────────────────┐
 *     │ Delegate the business logic to  │   │ Modify the UI element state in the   │
 *     │ the ViewModel                   │   │ UI directly                          │
 *     └─────────────────────────────────┘   └──────────────────────────────────────┘
 *
 *
 */

/**
 * BaseFragment 是一個抽象類別，封裝了共享的 Fragment 邏輯以及 ViewBinding。
 * B 是 ViewBinding 的型別參數。
 * VM 是 BaseViewModel 的型別參數。
 *
 * BaseFragment 提供了一種簡便的方式來處理 ViewBinding，並在 onCreateView 中自動創建和清理。
 * 並且還提供了四個抽象方法 (setupViews, addOnClickListener, handleState, initData)，讓子類可以根據自己的需求實現。
 */
abstract class BaseFragment<B : ViewBinding, VM : BaseViewModel<A, S>, A : Action, S : State>
    : Fragment() {

    private var _binding: B? = null
    protected val binding get() = _binding!!
    private var hasInitializedData = false

    /**
     * 透過 LayoutInflater、ViewGroup 和 Boolean 參數來創建 ViewBinding 的抽象方法。
     */
    abstract val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B

    /**
     * 用於監聽 UI State 的抽象 ViewModel，必須由子類實現。
     * 這裡，ViewModel 用於監聽 UI State，並提供對事件和狀態進行處理。
     * 注意，這裡的 ViewModel 必須是 VM 類型的，並需要由子類來實現。
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
     * onViewCreated 方法中，調用了四個抽象方法：setupViews，addOnClickListener，handleState 和 initData
     * setupViews，addOnClickListener，handleState 和 initData 分別用於設置視圖，設置視圖點擊事件，設置觀察者和初始化。
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 調用 Fragment 的父類方法，這是必須的，以保證視圖的生命周期正常運行
        super.onViewCreated(view, savedInstanceState)

        // 調用子類實現的 setupViews() 方法，用於設置視圖的各種屬性
        binding.setupViews()

        // 調用子類實現的 addOnClickListener() 方法，用於為視圖設置 OnClickListener
        binding.addOnClickListener()

        // 設置 States 觀察者，當 States 發生變化時，調用 handleState() 方法來處理這些狀態
        binding.handleState(viewLifecycleOwner, viewModel.states)

        // 該方法可能包括從導航參數取得數據，或進行其他任何需要在啟動時執行的初始化工作。
        // Tip: 若初始化工作為 ViewModel 操作之業務邏輯應當放置 ViewModel init{} 中。
        if (!hasInitializedData) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                binding.initData()
            }
            hasInitializedData = true
        }

    }

    /**
     * 設置視圖的抽象方法，由子類實現。
     */
    abstract fun B.setupViews()

    /**
     * 設置視圖 OnClickListener 的抽象方法，由子類實現。
     */
    abstract fun B.addOnClickListener()

    /**
     * 處理來至 ViewModel State Flow 的狀態，由子類實現。
     * 子類應該根據這些狀態來更新 UI 與處理 UI事件。
     */
    abstract fun B.handleState(viewLifecycleOwner: LifecycleOwner, states: StateFlow<S>)

    /**
     * 此方法由子類實現，用於初始化應用數據。這可能包括從導航參數取得數據，或進行其他任何需要在啟動時執行的初始化工作。
     * Tip: 若初始化工作為 ViewModel 操作之業務邏輯應當放置 ViewModel init{} 中。
     */
    protected open fun B.initData() {}

    /**
     * 在 onDestroyView 方法中，將 _binding 設置為 null，以避免內存泄露。
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

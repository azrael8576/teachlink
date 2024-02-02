package com.wei.teachlink.core.extensions.state

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * LiveDataEvents 是一種特殊的 MutableLiveData，它封裝了一種一次性事件。
 * 這種一次性事件的特性在於它只會被消費一次，而不會在配置變化（如旋轉屏幕）時被重複消費。
 * 這種一次性事件特別適用於 UI 事件，如顯示 Snackbar，導航到另一個頁面等。
 */
class LiveDataEvents<T> : MutableLiveData<List<T>>() {
    /**
     * 保存所有 ObserverWrapper 的集合。
     */
    private val observers = hashSetOf<ObserverWrapper<in T>>()

    /**
     * 觀察 LiveDataEvents。
     * 為每個觀察者創建一個 ObserverWrapper，並將它加入到觀察者集合中。
     */
    @MainThread
    override fun observe(
        owner: LifecycleOwner,
        observer: Observer<in List<T>>,
    ) {
        // 為每個觀察者創建一個觀察者包裝器，並將它加入到集合中
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        // 調用父類的 observe 方法，並將包裝器傳入，以便在值變化時通知到包裝器
        super.observe(owner, wrapper)
    }

    /**
     * 設置新的事件列表。
     * 會通知所有的 ObserverWrapper。
     */
    @MainThread
    override fun setValue(t: List<T>?) {
        // 當值變化時，通知每個觀察者包裝器新的值
        observers.forEach { it.newValue(t) }
        // 調用父類的 setValue 方法，更新值
        super.setValue(t)
    }

    /**
     * ObserverWrapper 是 Observer 的包裝器，專門用於處理一次性事件。
     * 它會在新的事件到來時，將事件添加到待處理事件列表中，並設置 pending 為 true。
     * 當事件被消費後，會將 pending 設為 false，並清空待處理事件列表。
     */
    private class ObserverWrapper<T>(val observer: Observer<in List<T>>) : Observer<List<T>> {
        /**
         * 一個標記，用於指示是否有新的待處理事件。
         */
        private val pending = AtomicBoolean(false)

        /**
         * 保存所有待處理事件的列表。
         */
        private val eventList = mutableListOf<List<T>>()

        /**
         * 當 LiveData 的值發生變化時調用。
         * 如果有待處理的事件，則消費事件並清空事件列表。
         */
        override fun onChanged(value: List<T>) {
            // 當有新的事件時，將標誌位設為 false，並調用真正的觀察者的 onChanged 方法
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(eventList.flatten())
                eventList.clear()
            }
        }

        /**
         * 當有新的事件到來時，將 pending 設為 true，並將新的事件添加到事件列表中。
         */
        fun newValue(t: List<T>?) {
            pending.set(true)
            t?.let {
                eventList.add(it)
            }
        }
    }
}

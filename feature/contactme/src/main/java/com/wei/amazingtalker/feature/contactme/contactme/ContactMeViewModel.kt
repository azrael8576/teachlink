package com.wei.amazingtalker.feature.contactme.contactme

import com.wei.amazingtalker.core.base.BaseViewModel
import com.wei.amazingtalker.feature.contactme.utilities.EMAIL
import com.wei.amazingtalker.feature.contactme.utilities.LINKEDIN_URL
import com.wei.amazingtalker.feature.contactme.utilities.NAME_ENG
import com.wei.amazingtalker.feature.contactme.utilities.NAME_TW
import com.wei.amazingtalker.feature.contactme.utilities.PHONE
import com.wei.amazingtalker.feature.contactme.utilities.POSITION
import com.wei.amazingtalker.feature.contactme.utilities.TIME_ZONE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactMeViewModel @Inject constructor() : BaseViewModel<
    ContactMeViewAction,
    ContactMeViewState,
    >(ContactMeViewState()) {

    init {
        getProfile()
    }

    private fun getProfile() {
        updateState {
            copy(
                nameTw = NAME_TW,
                nameEng = NAME_ENG,
                position = POSITION,
                phone = PHONE,
                linkedinUrl = LINKEDIN_URL,
                email = EMAIL,
                timeZone = TIME_ZONE,
            )
        }
    }

    private fun callPhone() {
        // TODO
    }

    override fun dispatch(action: ContactMeViewAction) {
        when (action) {
            is ContactMeViewAction.Call -> callPhone()
        }
    }
}

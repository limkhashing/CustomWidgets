package io.limkhashing.customwidgets.core

import android.os.Bundle
import io.limkhashing.customwidgets.extensions.DialogBuilder


open class BaseEvent()

class ShowDialogEvent(val dialogBuilder: DialogBuilder) : BaseEvent()
class DismissLoaderEvent() : BaseEvent()
class ShowToastEvent(val message: String) : BaseEvent()
class FinishEvent() : BaseEvent()
class StartActivityEvent<T>(val clazz:  Class<T>, val data: Bundle? = null): BaseEvent()
class StartActivityFlagEvent<T>(val clazz:  Class<T>, val data: Bundle? = null, val flag: Int): BaseEvent()
class StartDeepLinkActivityEvent(val uri:  String): BaseEvent()
class ShowDialogFragmentEvent(val baseDialogFragment: BaseDialogFragment, val fragmentTag: String) : BaseEvent()
class AddFragmentEvent(val baseFragment: BaseFragment, val fragmentTag: String) : BaseEvent()

class DismissFragmentEvent(val fragmentTag : String) : BaseEvent()
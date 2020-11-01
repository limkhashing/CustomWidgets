// Reference https://stackoverflow.com/questions/49901629/glide-showing-error-failed-to-find-generatedappglidemodule
// Placeholder class for managing all Glide. When using Glide, reference it by "GlideApp" instead of "Glide"
package io.limkhashing.customwidgets.utils.glide

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MapleGlideApp : AppGlideModule()

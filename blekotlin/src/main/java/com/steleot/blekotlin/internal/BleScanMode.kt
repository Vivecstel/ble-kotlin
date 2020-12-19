package com.steleot.blekotlin.internal

internal sealed class BleScanMode {

    object NoneMode : BleScanMode()

    object SingleMode : BleScanMode()

    object ListMode : BleScanMode()
}

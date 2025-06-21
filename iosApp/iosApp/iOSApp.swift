import SwiftUI

@main
struct iOSApp: App {

    init() {
        AppDiKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
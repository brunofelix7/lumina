package dev.brunofelix.lumina.presentation.util.extension

import dev.brunofelix.lumina.core.presentation.R
import dev.brunofelix.lumina.domain.util.exception.LocalException
import dev.brunofelix.lumina.domain.util.exception.RemoteException
import dev.brunofelix.lumina.presentation.util.UiText
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DataErrorExtTest : DescribeSpec({

    describe("RemoteException.toUiText") {
        it("should map every remote error to its message") {
            RemoteException.Unauthorized().toUiText() shouldBe UiText.StringResource(R.string.error_network_unauthorized)
            RemoteException.NotFound().toUiText() shouldBe UiText.StringResource(R.string.error_network_not_found)
            RemoteException.ServerError().toUiText() shouldBe UiText.StringResource(R.string.error_network_server)
            RemoteException.NoInternet().toUiText() shouldBe UiText.StringResource(R.string.error_network_no_internet)
            RemoteException.Unknown().toUiText() shouldBe UiText.StringResource(R.string.error_unknown)
            RemoteException.General(R.string.error_title).toUiText() shouldBe UiText.StringResource(R.string.error_title)
        }

        it("should keep the API message of an ApiError") {
            RemoteException.ApiError(code = 422, message = "Invalid deck").toUiText() shouldBe
                UiText.DynamicString("Invalid deck")
        }
    }

    describe("LocalException.toUiText") {
        it("should map every local error to its message") {
            LocalException.DatabaseError().toUiText() shouldBe UiText.StringResource(R.string.error_local_database)
            LocalException.PermissionDenied().toUiText() shouldBe
                UiText.StringResource(R.string.error_local_permission_denied)
            LocalException.DiskFull().toUiText() shouldBe UiText.StringResource(R.string.error_local_disk_full)
            LocalException.Unknown().toUiText() shouldBe UiText.StringResource(R.string.error_unknown)
            LocalException.General(R.string.error_title).toUiText() shouldBe UiText.StringResource(R.string.error_title)
        }
    }

    describe("Throwable.toUiText") {
        it("should delegate to the remote and local mappings") {
            val remote: Throwable = RemoteException.NoInternet()
            val local: Throwable = LocalException.DiskFull()

            remote.toUiText() shouldBe UiText.StringResource(R.string.error_network_no_internet)
            local.toUiText() shouldBe UiText.StringResource(R.string.error_local_disk_full)
        }

        it("should fall back to the unknown error for any other throwable") {
            IllegalStateException("boom").toUiText() shouldBe UiText.StringResource(R.string.error_unknown)
        }
    }
})

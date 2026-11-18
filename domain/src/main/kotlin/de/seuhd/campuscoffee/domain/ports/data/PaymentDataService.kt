package de.seuhd.campuscoffee.domain.ports.data

import de.seuhd.campuscoffee.domain.model.Payment
import java.util.UUID

/** Data port for recorded payments. */
interface PaymentDataService : CrudDataService<Payment, UUID>

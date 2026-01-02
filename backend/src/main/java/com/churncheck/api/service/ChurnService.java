private PredictionRequestDTO mapToPredictionRequest(Client client) {

    Integer gender =
        client.getGender() == Gender.MALE ? 0 :
        client.getGender() == Gender.FEMALE ? 1 : 0;

    byte hasPhone = client.getClientPhone() != null ? (byte) 1 : (byte) 0;

    return new PredictionRequestDTO(
        gender,                                         // Integer
        client.getNearLocation().byteValue(),            // Byte
        client.getPartnerEmployee().byteValue(),         // Byte
        client.getPromoFriends().byteValue(),            // Byte
        hasPhone,                                        // Byte
        client.getContractPeriod().byteValue(),          // Byte
        client.getGroupVisits().byteValue(),             // Byte
        client.getAge(),                                 // Integer
        client.getAvgAdditionalChargesTotal(),            // Double
        client.getMonthToEndContract(),                   // Integer
        client.getLifetimeMonths(),                       // Integer
        client.getAvgClassFrequencyTotal().doubleValue(), // Double
        client.getAvgClassFrequencyCurrentMonth().doubleValue() // Double
    );
}

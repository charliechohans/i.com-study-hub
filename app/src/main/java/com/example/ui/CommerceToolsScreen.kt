package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.pow

private data class AccountClassificationItem(
    val accountName: String,
    val classicType: String,
    val modernCategory: String,
    val increaseSide: String,
    val decreaseSide: String,
    val goldenRuleApplied: String
)

private val sampleAccounts = listOf(
    AccountClassificationItem(
        "Cash / Bank / Building / Machinery",
        "Real Account (حقیقی کھاتہ)",
        "Asset (اثاثہ)",
        "Debit (Dr)",
        "Credit (Cr)",
        "Debit what comes in, Credit what goes out"
    ),
    AccountClassificationItem(
        "Accounts Receivable (Debtors) / Ali's A/c",
        "Personal Account (شخصی کھاتہ)",
        "Asset / Party",
        "Debit (Dr)",
        "Credit (Cr)",
        "Debit the receiver, Credit the giver"
    ),
    AccountClassificationItem(
        "Accounts Payable (Creditors) / Bank Loan",
        "Personal Account (شخصی کھاتہ)",
        "Liability (واجب الادا)",
        "Credit (Cr)",
        "Debit (Dr)",
        "Debit the receiver, Credit the giver"
    ),
    AccountClassificationItem(
        "Purchases / Salaries / Rent / Wages / Depreciation",
        "Nominal Account (اضافی کھاتہ)",
        "Expense / Loss (اخراجات)",
        "Debit (Dr)",
        "Credit (Cr)",
        "Debit all expenses & losses, Credit all incomes & gains"
    ),
    AccountClassificationItem(
        "Sales / Commission Received / Interest Earned",
        "Nominal Account (اضافی کھاتہ)",
        "Revenue / Income (آمدنی)",
        "Credit (Cr)",
        "Debit (Dr)",
        "Debit all expenses & losses, Credit all incomes & gains"
    ),
    AccountClassificationItem(
        "Owner's Capital (سرمایہ)",
        "Personal Account",
        "Owner's Equity",
        "Credit (Cr)",
        "Debit (Dr)",
        "Credit the giver (Owner investing in business)"
    ),
    AccountClassificationItem(
        "Drawings (ذاتی استعمال کے لیے رقم/مال)",
        "Personal Account",
        "Contra Owner's Equity",
        "Debit (Dr)",
        "Credit (Cr)",
        "Debit the receiver (Owner withdrawing for personal use)"
    )
)

@Composable
fun CommerceToolsScreen() {
    var selectedToolIndex by remember { mutableIntStateOf(0) }
    val tools = listOf(
        "Dr / Cr Rules",
        "Accounting Eq.",
        "Business Math",
        "Depreciation"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("commerce_tools_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Interactive Commerce Lab",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Practical calculators & golden rules for I.Com Accounting & Business Math",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tools.forEachIndexed { index, label ->
                    FilterChip(
                        selected = selectedToolIndex == index,
                        onClick = { selectedToolIndex = index },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.testTag("tool_chip_$index")
                    )
                }
            }
        }

        when (selectedToolIndex) {
            0 -> {
                item {
                    GoldenRulesBanner()
                }
                items(sampleAccounts) { item ->
                    AccountRuleCard(item)
                }
            }
            1 -> {
                item {
                    AccountingEquationCalculator()
                }
            }
            2 -> {
                item {
                    BusinessMathInterestCalculator()
                }
            }
            3 -> {
                item {
                    DepreciationCalculatorCard()
                }
            }
        }
    }
}

@Composable
private fun GoldenRulesBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Balance,
                    contentDescription = "Golden Rules",
                    tint = Color(0xFFFBBF24)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "3 Golden Rules of Accounting (اصولِ محاسبہ)",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
            Text(
                text = "1. Personal A/c: Debit the Receiver, Credit the Giver\n" +
                    "2. Real A/c: Debit what Comes In, Credit what Goes Out\n" +
                    "3. Nominal A/c: Debit all Expenses & Losses, Credit all Incomes & Gains",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun AccountRuleCard(item: AccountClassificationItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.accountName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = item.modernCategory,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "British Classification: ${item.classicType}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF059669).copy(alpha = 0.12f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "When Increases (+)",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF059669)
                        )
                        Text(
                            text = item.increaseSide,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFD97706).copy(alpha = 0.12f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "When Decreases (-)",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFFD97706)
                        )
                        Text(
                            text = item.decreaseSide,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rule: ${item.goldenRuleApplied}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun AccountingEquationCalculator() {
    var capitalInput by remember { mutableStateOf("500000") }
    var liabilitiesInput by remember { mutableStateOf("180000") }
    var revenueInput by remember { mutableStateOf("95000") }
    var expensesInput by remember { mutableStateOf("45000") }
    var drawingsInput by remember { mutableStateOf("15000") }

    val capital = capitalInput.toDoubleOrNull() ?: 0.0
    val liabilities = liabilitiesInput.toDoubleOrNull() ?: 0.0
    val revenue = revenueInput.toDoubleOrNull() ?: 0.0
    val expenses = expensesInput.toDoubleOrNull() ?: 0.0
    val drawings = drawingsInput.toDoubleOrNull() ?: 0.0

    val netProfit = revenue - expenses
    val closingEquity = capital + netProfit - drawings
    val totalAssets = liabilities + closingEquity

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Accounting Equation",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Accounting Equation & Single Entry Solver",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = "Assets = Liabilities + [Opening Capital + (Revenue - Expenses) - Drawings]",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            OutlinedTextField(
                value = capitalInput,
                onValueChange = { capitalInput = it },
                label = { Text("Opening Capital (Rs.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = liabilitiesInput,
                onValueChange = { liabilitiesInput = it },
                label = { Text("External Liabilities / Creditors (Rs.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = revenueInput,
                    onValueChange = { revenueInput = it },
                    label = { Text("Total Revenue (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = expensesInput,
                    onValueChange = { expensesInput = it },
                    label = { Text("Total Expenses (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = drawingsInput,
                onValueChange = { drawingsInput = it },
                label = { Text("Owner's Drawings (Rs.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Net Profit / (Loss): Rs. ${String.format(Locale.US, "%,.0f", netProfit)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Closing Owner's Equity: Rs. ${String.format(Locale.US, "%,.0f", closingEquity)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Total Assets (Equilibrium): Rs. ${String.format(Locale.US, "%,.0f", totalAssets)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun BusinessMathInterestCalculator() {
    var principalInput by remember { mutableStateOf("50000") }
    var rateInput by remember { mutableStateOf("12") }
    var yearsInput by remember { mutableStateOf("3") }

    val p = principalInput.toDoubleOrNull() ?: 0.0
    val r = (rateInput.toDoubleOrNull() ?: 0.0) / 100.0
    val t = yearsInput.toDoubleOrNull() ?: 0.0

    val simpleInterest = p * r * t
    val simpleAmount = p + simpleInterest
    val compoundAmount = if (p > 0 && t >= 0) p * (1.0 + r).pow(t) else 0.0
    val compoundInterest = compoundAmount - p

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = "Business Math Calculator",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "I.Com Part 1: Simple & Compound Interest",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = "Simple: I = P × r × t   |   Compound: CI = P[(1 + i)ⁿ - 1]",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            OutlinedTextField(
                value = principalInput,
                onValueChange = { principalInput = it },
                label = { Text("Principal Amount P (Rs.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = rateInput,
                    onValueChange = { rateInput = it },
                    label = { Text("Annual Rate r (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = yearsInput,
                    onValueChange = { yearsInput = it },
                    label = { Text("Time Period t (Years)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Simple Interest (SI): Rs. ${String.format(Locale.US, "%,.2f", simpleInterest)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Simple Total Amount (S): Rs. ${String.format(Locale.US, "%,.2f", simpleAmount)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    HorizontalDivider()
                    Text(
                        text = "Compound Interest (CI): Rs. ${String.format(Locale.US, "%,.2f", compoundInterest)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Compound Amount S = P(1+i)ⁿ: Rs. ${String.format(Locale.US, "%,.2f", compoundAmount)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun DepreciationCalculatorCard() {
    var costInput by remember { mutableStateOf("250000") }
    var scrapInput by remember { mutableStateOf("25000") }
    var lifeInput by remember { mutableStateOf("5") }
    var rateInput by remember { mutableStateOf("20") }

    val cost = costInput.toDoubleOrNull() ?: 0.0
    val scrap = scrapInput.toDoubleOrNull() ?: 0.0
    val life = (lifeInput.toDoubleOrNull() ?: 1.0).coerceAtLeast(1.0)
    val rate = (rateInput.toDoubleOrNull() ?: 0.0) / 100.0

    val straightLineAnnual = (cost - scrap) / life
    val wdvYear1 = cost * rate
    val bookValueEndYear1 = cost - wdvYear1
    val wdvYear2 = bookValueEndYear1 * rate
    val bookValueEndYear2 = bookValueEndYear1 - wdvYear2

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingDown,
                    contentDescription = "Depreciation Calculator",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "I.Com Part 2: Depreciation Schedule",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = "Compare Straight-Line (Fixed Installment) vs. Diminishing Balance (WDV) Method",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = costInput,
                onValueChange = { costInput = it },
                label = { Text("Original Cost of Machinery (Rs.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = scrapInput,
                    onValueChange = { scrapInput = it },
                    label = { Text("Scrap Value (Rs.)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = lifeInput,
                    onValueChange = { lifeInput = it },
                    label = { Text("Useful Life (Yrs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = rateInput,
                    onValueChange = { rateInput = it },
                    label = { Text("WDV Rate (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "1. Straight Line Method (Every Year):",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Annual Depreciation = Rs. ${String.format(Locale.US, "%,.0f", straightLineAnnual)} / year",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "2. Diminishing Balance (WDV) Method:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "• Year 1 Depr: Rs. ${String.format(Locale.US, "%,.0f", wdvYear1)}  (Closing BV: Rs. ${String.format(Locale.US, "%,.0f", bookValueEndYear1)})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "• Year 2 Depr: Rs. ${String.format(Locale.US, "%,.0f", wdvYear2)}  (Closing BV: Rs. ${String.format(Locale.US, "%,.0f", bookValueEndYear2)})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

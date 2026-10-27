package com.example.data

data class IComSubject(
    val code: String,
    val title: String,
    val urduSubtitle: String,
    val part: String, // "PART_1" or "PART_2"
    val totalMarks: Int,
    val passingMarks: Int,
    val keyTopics: List<String>,
    val description: String,
    val chapters: List<SubjectChapter>,
    val builtInFlashcards: List<BuiltInFlashcard>,
    val builtInMcqs: List<McqQuestion>
)

data class SubjectChapter(
    val number: Int,
    val title: String,
    val summary: String,
    val keyFormulasOrRules: List<String>,
    val importantQuestions: List<String>
)

data class BuiltInFlashcard(
    val id: String,
    val subjectCode: String,
    val part: String,
    val question: String,
    val answer: String,
    val tag: String
)

data class McqQuestion(
    val id: String,
    val subjectCode: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

object IComCurriculumData {

    val subjects: List<IComSubject> = listOf(
        // ==================== I.COM PART 1 ====================
        IComSubject(
            code = "ACC_1",
            title = "Principles of Accounting I",
            urduSubtitle = "اصولِ محاسبہ (سالِ اول)",
            part = "PART_1",
            totalMarks = 100,
            passingMarks = 33,
            keyTopics = listOf("Journal & Ledger", "Trial Balance", "Bank Reconciliation", "Bills of Exchange", "Final Accounts"),
            description = "Foundation of double-entry bookkeeping, journalizing, ledger posting, cash book, bank reconciliation statement, and preparation of Final Accounts with adjustments.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Introduction to Accounting & Accounting Equation",
                    summary = "Covers business transactions, double-entry system, and the fundamental accounting equation showing how every transaction affects Assets, Liabilities, and Owner's Equity.",
                    keyFormulasOrRules = listOf(
                        "Assets = Liabilities + Owner's Equity (Capital)",
                        "Owner's Equity = Capital + Revenue - Expenses - Drawings",
                        "Real Account Rule: Debit what comes in, Credit what goes out",
                        "Personal Account Rule: Debit the receiver, Credit the giver",
                        "Nominal Account Rule: Debit all expenses & losses, Credit all incomes & gains"
                    ),
                    importantQuestions = listOf(
                        "Define Accounting and state its main objectives for a business.",
                        "Differentiate between Bookkeeping and Accounting.",
                        "Explain Money Measurement Concept and Business Entity Concept."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Journal, Ledger & Trial Balance",
                    summary = "Recording transactions chronologically in the General Journal (Book of Original Entry), classifying them into Ledger accounts (T-Accounts), and verifying arithmetic accuracy via Trial Balance.",
                    keyFormulasOrRules = listOf(
                        "Assets & Expenses: Increase = Debit (Dr), Decrease = Credit (Cr)",
                        "Liabilities, Capital & Revenue: Increase = Credit (Cr), Decrease = Debit (Dr)",
                        "Compound Entry: An entry involving more than one Debit or more than one Credit"
                    ),
                    importantQuestions = listOf(
                        "Why is Journal called the Book of Original Entry and Ledger the King of all Books?",
                        "What are the limitations of a Trial Balance? State errors not disclosed by Trial Balance."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Cash Book & Bank Reconciliation Statement (BRS)",
                    summary = "Three-column Cash Book (Cash, Bank, Discount) and reconciling differences between Cash Book (Bank Column) and Bank Pass Book / Statement.",
                    keyFormulasOrRules = listOf(
                        "Cash Book Dr. Balance = Favorable (Deposit) | Cr. Balance = Overdraft (Unfavorable)",
                        "Pass Book Cr. Balance = Favorable | Dr. Balance = Overdraft",
                        "Unpresented Cheques: Issued by trader but not yet presented to bank (Add to Dr. Cash Book)",
                        "Uncredited Cheques: Deposited into bank but not yet collected/cleared (Deduct from Dr. Cash Book)"
                    ),
                    importantQuestions = listOf(
                        "What is a Contra Entry? Give two examples recorded in a Three-Column Cash Book.",
                        "List 6 common causes of disagreement between Cash Book and Bank Statement."
                    )
                ),
                SubjectChapter(
                    number = 4,
                    title = "Bills of Exchange",
                    summary = "Accounting treatment for Trade Bills, Accommodation Bills, Discounting, Endorsement, Dishonor, Noting Charges, and Renewal of Bills.",
                    keyFormulasOrRules = listOf(
                        "Days of Grace = 3 Days added to the nominal tenure of a Time Bill",
                        "Drawer's Entry on Acceptance: Bills Receivable A/c (Dr) To Drawee A/c (Cr)",
                        "Drawee's Entry on Acceptance: Drawer A/c (Dr) To Bills Payable A/c (Cr)"
                    ),
                    importantQuestions = listOf(
                        "Define a Bill of Exchange and differentiate it from a Promissory Note.",
                        "What is meant by Retiring a Bill under Rebate and Dishonor of a Bill?"
                    )
                ),
                SubjectChapter(
                    number = 5,
                    title = "Final Accounts with Adjustments",
                    summary = "Preparation of Trading Account, Profit & Loss Account, and Balance Sheet for a sole proprietor along with year-end adjustments.",
                    keyFormulasOrRules = listOf(
                        "Gross Profit = Net Sales - Cost of Goods Sold (COGS)",
                        "COGS = Opening Stock + Net Purchases + Direct Expenses - Closing Stock",
                        "Net Profit = Gross Profit + Operating/Other Incomes - Indirect Expenses",
                        "Closing Stock Adjustment: (1) Credit Trading A/c, (2) Show as Current Asset in Balance Sheet",
                        "Outstanding Expenses: (1) Add to Expense in Trading/P&L, (2) Show as Current Liability"
                    ),
                    importantQuestions = listOf(
                        "Distinguish between Capital Expenditure and Revenue Expenditure with examples.",
                        "Explain the double treatment of Provision for Doubtful Debts and Prepaid Expenses."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_acc1_1", "ACC_1", "PART_1", "Golden Rule for Real Accounts?", "Debit what comes in, Credit what goes out.", "Golden Rules"),
                BuiltInFlashcard("fc_acc1_2", "ACC_1", "PART_1", "Golden Rule for Nominal Accounts?", "Debit all expenses and losses, Credit all incomes and gains.", "Golden Rules"),
                BuiltInFlashcard("fc_acc1_3", "ACC_1", "PART_1", "Formula for Cost of Goods Sold (COGS)?", "Opening Stock + Net Purchases + Direct Expenses - Closing Stock", "Final Accounts"),
                BuiltInFlashcard("fc_acc1_4", "ACC_1", "PART_1", "What is a Contra Entry in Cash Book?", "A transaction affecting both Cash and Bank columns (e.g., cash deposited into bank or cash withdrawn for office use), marked with 'C' in L.F.", "Cash Book"),
                BuiltInFlashcard("fc_acc1_5", "ACC_1", "PART_1", "How many Days of Grace are allowed on a Bill of Exchange?", "Exactly 3 Days are added to the due date of a time bill of exchange.", "Bills of Exchange")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_acc1_1", "ACC_1", "Which book is known as the 'King of all Books' in Accounting?", listOf("General Journal", "Ledger", "Cash Book", "Trial Balance"), 1, "The Ledger is called the King of all Books because all journal entries are finally classified and summarized into ledger accounts."),
                McqQuestion("mcq_acc1_2", "ACC_1", "Credit balance in the Bank column of the Cash Book indicates:", listOf("Favorable balance", "Cash in hand", "Bank Overdraft", "Net Profit"), 2, "Normally Bank column has a Debit balance. A Credit balance means more money was withdrawn than deposited (Bank Overdraft)."),
                McqQuestion("mcq_acc1_3", "ACC_1", "Wages paid for the installation of new machinery should be debited to:", listOf("Wages Account", "Machinery Account", "Repairs Account", "Trading Account"), 1, "Any expenditure incurred to bring a fixed asset into working condition is a Capital Expenditure and is debited to the Asset (Machinery) Account."),
                McqQuestion("mcq_acc1_4", "ACC_1", "A Bill of Exchange has how many parties?", listOf("Two", "Three (Drawer, Drawee, Payee)", "Four", "Five"), 1, "A Bill of Exchange involves three parties: Drawer (maker), Drawee (acceptor), and Payee (receiver of payment)."),
                McqQuestion("mcq_acc1_5", "ACC_1", "Prepaid Insurance given in adjustments is shown in the Balance Sheet as a:", listOf("Current Liability", "Current Asset", "Fixed Asset", "Intangible Asset"), 1, "Expenses paid in advance (Prepaid Expenses) provide future benefit within a year, so they are recorded as Current Assets.")
            )
        ),

        IComSubject(
            code = "POC_1",
            title = "Principles of Commerce",
            urduSubtitle = "اصولِ تجارت (سالِ اول)",
            part = "PART_1",
            totalMarks = 75,
            passingMarks = 25,
            keyTopics = listOf("Sole Proprietorship", "Partnership", "Joint Stock Company", "Cooperative Society", "Trade & Auxiliaries"),
            description = "Comprehensive study of business organizations (Sole Tradership, Partnership, Joint Stock Company), formation documents, marketing, wholesale & retail trade, and office management.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Nature & Scope of Commerce",
                    summary = "Meaning of Business, Industry (Primary, Secondary, Service), Commerce (Trade and Aids to Trade like Banking, Insurance, Transport, Warehousing).",
                    keyFormulasOrRules = listOf(
                        "Business = Industry + Commerce",
                        "Commerce = Trade (Home & Foreign) + Auxiliaries to Trade",
                        "Economic Activities aim at earning profit through legal goods & services"
                    ),
                    importantQuestions = listOf(
                        "Define Commerce and explain its scope and importance in Pakistan.",
                        "What are the qualities of a successful businessman?"
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Sole Proprietorship & Partnership",
                    summary = "Features, merits, and demerits of Sole Proprietorship; Partnership Act 1932, Partnership Deed, kinds of partners, registration, and dissolution of firm.",
                    keyFormulasOrRules = listOf(
                        "Sole Proprietorship: 1 Owner, Unlimited Liability, No separate legal entity",
                        "Partnership Members: Minimum 2, Maximum 20 (10 for banking business under classic curriculum)",
                        "Partnership Liability: Unlimited, joint and several for all general partners"
                    ),
                    importantQuestions = listOf(
                        "Explain the merits and demerits of Sole Proprietorship. Why is it the most popular form of business?",
                        "What is a Partnership Deed? State its important contents.",
                        "Explain the different kinds of Partners (Active, Sleeping, Nominal, Minor, Secret)."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Joint Stock Company",
                    summary = "Definition, features, classification of companies, formation stages (Promotion, Incorporation, Capital Subscription, Commencement), Memorandum & Articles of Association, Prospectus.",
                    keyFormulasOrRules = listOf(
                        "Private Ltd Company: Min 2, Max 50 members; restriction on transfer of shares",
                        "Public Ltd Company: Min 7 members, Max Unlimited; shares freely transferable on Stock Exchange",
                        "Three Key Documents: Memorandum of Association (MOA), Articles of Association (AOA), Prospectus"
                    ),
                    importantQuestions = listOf(
                        "Differentiate between a Private Limited Company and a Public Limited Company (at least 8 points).",
                        "Compare Memorandum of Association (MOA) and Articles of Association (AOA).",
                        "Explain the steps in the formation of a Joint Stock Company."
                    )
                ),
                SubjectChapter(
                    number = 4,
                    title = "Home Trade, Foreign Trade & Chamber of Commerce",
                    summary = "Wholesaler vs Retailer, Export & Import procedures, Bill of Lading, Charter Party, Letter of Credit (L/C), and role of Chamber of Commerce.",
                    keyFormulasOrRules = listOf(
                        "Home Trade = Wholesale Trade + Retail Trade",
                        "Foreign Trade = Import Trade + Export Trade + Entrepot Trade",
                        "Letter of Credit (L/C): Bank guarantee issued by importer's bank to exporter"
                    ),
                    importantQuestions = listOf(
                        "What services does a Wholesaler render to Manufacturers and Retailers?",
                        "Explain the step-by-step Import and Export procedure.",
                        "What are the functions of a Chamber of Commerce and Industry?"
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_poc1_1", "POC_1", "PART_1", "Which Act governs Partnership in Pakistan?", "The Partnership Act, 1932.", "Partnership"),
                BuiltInFlashcard("fc_poc1_2", "POC_1", "PART_1", "What is the Charter of a Joint Stock Company?", "The Memorandum of Association (MOA) — it defines the external powers, objectives, and scope of the company.", "Company"),
                BuiltInFlashcard("fc_poc1_3", "POC_1", "PART_1", "What is Entrepot Trade?", "Importing goods from one country with the purpose of re-exporting them to another country.", "Foreign Trade"),
                BuiltInFlashcard("fc_poc1_4", "POC_1", "PART_1", "Who is a Sleeping (Dormant) Partner?", "A partner who invests capital and shares profits/losses but does not take active part in the day-to-day management of the firm.", "Partnership")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_poc1_1", "POC_1", "The liability of a Sole Proprietor is:", listOf("Limited to capital", "Unlimited", "Limited by guarantee", "Zero"), 1, "A sole proprietor is personally liable for all business debts, even from personal property if business assets are insufficient."),
                McqQuestion("mcq_poc1_2", "POC_1", "Which document contains rules and regulations for the internal management of a company?", listOf("Prospectus", "Memorandum of Association", "Articles of Association", "Certificate of Incorporation"), 2, "Articles of Association (AOA) govern the internal management and affairs of the company."),
                McqQuestion("mcq_poc1_3", "POC_1", "A Public Limited Company must have at least how many minimum members?", listOf("2", "7", "10", "20"), 1, "A Public Limited Company requires a minimum of 7 members and has no upper limit on maximum members."),
                McqQuestion("mcq_poc1_4", "POC_1", "Which document is an invitation to the general public to subscribe for shares or debentures?", listOf("Statement in lieu", "Prospectus", "Partnership Deed", "Bill of Lading"), 1, "A Prospectus is issued by a Public Limited Company inviting the public to purchase its shares or debentures.")
            )
        ),

        IComSubject(
            code = "ECO_1",
            title = "Principles of Economics",
            urduSubtitle = "اصولِ معاشیات (سالِ اول)",
            part = "PART_1",
            totalMarks = 75,
            passingMarks = 25,
            keyTopics = listOf("Consumer Behavior & Utility", "Demand & Supply", "Market Equilibrium", "Laws of Returns", "National Income & Money"),
            description = "Microeconomics and Macroeconomics fundamentals: Adam Smith, Marshall & Robbins definitions, Law of Diminishing Marginal Utility, Demand & Supply elasticity, Cost & Revenue curves, Perfect Competition & Monopoly.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Definitions of Economics (Smith, Marshall, Robbins)",
                    summary = "Evolution of Economics from Classical School (Science of Wealth by Adam Smith, 1776) to Neo-Classical (Science of Material Welfare by Alfred Marshall, 1890) and Modern (Science of Scarcity & Choice by Lionel Robbins, 1932).",
                    keyFormulasOrRules = listOf(
                        "Adam Smith (1776): 'Wealth of Nations' — Production, Exchange, Distribution & Consumption of Wealth",
                        "Alfred Marshall (1890): 'Principles of Economics' — Study of mankind in the ordinary business of life",
                        "Lionel Robbins (1932): Human behavior as relationship between Unlimited Ends and Scarce Means with alternative uses"
                    ),
                    importantQuestions = listOf(
                        "Critically examine Robbins' definition of Economics as a Science of Scarcity and Choice.",
                        "Compare Marshall's and Robbins' definitions of Economics."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Consumer Behavior: Law of Diminishing Marginal Utility",
                    summary = "Total Utility (TU) vs Marginal Utility (MU), Law of Diminishing Marginal Utility, and Law of Equi-Marginal Utility.",
                    keyFormulasOrRules = listOf(
                        "MU = ΔTU / ΔQ (Change in Total Utility divided by Change in Quantity)",
                        "When MU is positive -> TU increases",
                        "When MU is zero (Point of Satiety) -> TU is Maximum",
                        "When MU becomes negative -> TU starts falling"
                    ),
                    importantQuestions = listOf(
                        "State and explain the Law of Diminishing Marginal Utility with a schedule and diagram. Also state its assumptions and limitations.",
                        "Explain the Law of Equi-Marginal Utility and its practical importance."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Demand, Supply & Price Elasticity",
                    summary = "Law of Demand, Law of Supply, Market Equilibrium, and measurement of Price Elasticity of Demand (Unity, More than Unity, Less than Unity).",
                    keyFormulasOrRules = listOf(
                        "Law of Demand: P ↑ => Qd ↓ and P ↓ => Qd ↑ (Inverse relationship, ceteris paribus)",
                        "Law of Supply: P ↑ => Qs ↑ and P ↓ => Qs ↓ (Direct relationship)",
                        "Price Elasticity of Demand (Ed) = (% Change in Quantity Demanded) / (% Change in Price)",
                        "Market Equilibrium: Quantity Demanded (Qd) = Quantity Supplied (Qs)"
                    ),
                    importantQuestions = listOf(
                        "State and explain the Law of Demand with schedule and diagram. What are its assumptions?",
                        "Explain how Price is determined under Perfect Competition through Demand and Supply equilibrium."
                    )
                ),
                SubjectChapter(
                    number = 4,
                    title = "National Income, Money & Inflation",
                    summary = "Concepts of GDP, GNP, NNP, NI, Personal Income, Disposable Personal Income, Barter System difficulties, Functions of Money, and Quantity Theory of Money.",
                    keyFormulasOrRules = listOf(
                        "GNP = GDP + Net Factor Income from Abroad (NFIA)",
                        "NNP = GNP - Depreciation Allowance",
                        "National Income (NI) = NNP at Market Price - Indirect Taxes + Subsidies",
                        "Fisher's Equation of Exchange: MV = PT (or P = [MV + M'V'] / T)"
                    ),
                    importantQuestions = listOf(
                        "Define National Income and explain the various concepts of National Income (GDP, GNP, NNP, NI, PI, DPI).",
                        "Explain the methods of measuring National Income and difficulties in its measurement."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_eco1_1", "ECO_1", "PART_1", "When Marginal Utility (MU) is zero, what is Total Utility (TU)?", "Total Utility (TU) is at its Maximum (Point of Saturation / Satiety).", "Utility"),
                BuiltInFlashcard("fc_eco1_2", "ECO_1", "PART_1", "Who wrote the book 'An Inquiry into the Nature and Causes of the Wealth of Nations' (1776)?", "Adam Smith (Father of Economics).", "Definitions"),
                BuiltInFlashcard("fc_eco1_3", "ECO_1", "PART_1", "Formula linking GNP and GDP?", "GNP = GDP + Net Factor Income from Abroad (Income earned by nationals abroad minus income earned by foreigners domestically).", "National Income"),
                BuiltInFlashcard("fc_eco1_4", "ECO_1", "PART_1", "Condition for Firm's Equilibrium under Perfect Competition & Monopoly?", "1) Marginal Cost = Marginal Revenue (MC = MR), and 2) MC curve must cut MR curve from below.", "Market Structure")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_eco1_1", "ECO_1", "Who defined Economics as a 'Science of Scarcity and Choice' in 1932?", listOf("Adam Smith", "Alfred Marshall", "Lionel Robbins", "J.M. Keynes"), 2, "Prof. Lionel Robbins defined Economics in terms of unlimited ends and scarce means having alternative uses in his 1932 book."),
                McqQuestion("mcq_eco1_2", "ECO_1", "The slope of a normal Demand Curve is:", listOf("Positive (upward)", "Negative (downward left to right)", "Vertical", "Horizontal"), 1, "Because price and quantity demanded have an inverse relationship, the demand curve slopes downward from left to right (negative slope)."),
                McqQuestion("mcq_eco1_3", "ECO_1", "When Total Utility starts decreasing, Marginal Utility is:", listOf("Zero", "Maximum", "Positive", "Negative"), 3, "Once consumption exceeds the point of satiety, Marginal Utility becomes negative and Total Utility begins to decline."),
                McqQuestion("mcq_eco1_4", "ECO_1", "GNP minus Depreciation is equal to:", listOf("GDP", "NNP (Net National Product)", "Personal Income", "Disposable Income"), 1, "Deducting capital consumption (depreciation allowance) from Gross National Product gives Net National Product (NNP).")
            )
        ),

        IComSubject(
            code = "BM_1",
            title = "Business Mathematics",
            urduSubtitle = "تجارتی ریاضی (سالِ اول)",
            part = "PART_1",
            totalMarks = 50,
            passingMarks = 17,
            keyTopics = listOf("Ratio & Proportion", "Percentage & Commission", "Simple & Compound Interest", "Annuities", "Linear & Quadratic Equations", "Matrices"),
            description = "Essential quantitative math for commerce: Ratio, Proportion, Profit & Loss, Trade & Cash Discount, Simple & Compound Interest, Annuities, Equations, Functions, and Matrices with Determinants.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Ratio, Proportion & Percentage",
                    summary = "Direct, Inverse, and Compound Proportion, Partnership profit distribution by capital-time ratio, Markup, Markdown, Trade Discount, and Commission.",
                    keyFormulasOrRules = listOf(
                        "Proportion: If a : b :: c : d, then Product of Extremes (a × d) = Product of Means (b × c)",
                        "Profit % = (Profit / Cost Price) × 100",
                        "Loss % = (Loss / Cost Price) × 100",
                        "Net Price = List Price × (1 - Trade Discount Rate)"
                    ),
                    importantQuestions = listOf(
                        "Three partners invest Rs. 40,000, Rs. 60,000, and Rs. 80,000. Distribute a profit of Rs. 54,000 in ratio of their capitals.",
                        "Find the single discount equivalent to successive discounts of 20% and 10%."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Simple Interest, Compound Interest & Annuities",
                    summary = "Mathematics of finance covering time value of money, Simple Interest, Compound Amount, Effective Rate, and Ordinary Annuity.",
                    keyFormulasOrRules = listOf(
                        "Simple Interest: I = P × r × t  |  Amount S = P(1 + rt)",
                        "Compound Amount: S = P(1 + i)^n",
                        "Compound Interest: CI = P[(1 + i)^n - 1]",
                        "Future Value of Ordinary Annuity: S = R × [((1 + i)^n - 1) / i]",
                        "Present Value of Ordinary Annuity: A = R × [(1 - (1 + i)^(-n)) / i]"
                    ),
                    importantQuestions = listOf(
                        "Find the compound interest on Rs. 50,000 for 3 years at 10% per annum compounded semi-annually.",
                        "Calculate the future value of an ordinary annuity of Rs. 5,000 paid at the end of each year for 5 years at 8% compounded annually."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Linear, Quadratic Equations & Matrices",
                    summary = "Solving first-degree and quadratic equations, break-even analysis, Matrix addition, multiplication, transpose, determinant, adjoint, inverse, and Cramer's Rule.",
                    keyFormulasOrRules = listOf(
                        "Quadratic Formula: x = [-b ± √(b² - 4ac)] / (2a)",
                        "Discriminant = b² - 4ac",
                        "For 2×2 Matrix A = [[a, b], [c, d]], Determinant |A| = ad - bc",
                        "Adjoint Adj(A) = [[d, -b], [-c, a]]  |  Inverse A⁻¹ = Adj(A) / |A| (where |A| ≠ 0)"
                    ),
                    importantQuestions = listOf(
                        "Solve system of linear equations using Matrix Inversion Method and Cramer's Rule.",
                        "Define Singular and Non-Singular Matrix, Symmetric Matrix, and Identity Matrix."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_bm1_1", "BM_1", "PART_1", "Formula for Compound Amount (S)?", "S = P(1 + i)^n, where P = Principal, i = interest rate per conversion period, n = total number of periods.", "Finance Math"),
                BuiltInFlashcard("fc_bm1_2", "BM_1", "PART_1", "When is a square matrix A called a Singular Matrix?", "When its determinant is equal to zero (|A| = 0). A singular matrix does not have an inverse.", "Matrices"),
                BuiltInFlashcard("fc_bm1_3", "BM_1", "PART_1", "Quadratic Formula for ax² + bx + c = 0?", "x = [-b ± √(b² - 4ac)] / (2a)", "Equations")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_bm1_1", "BM_1", "If |A| = 0, then Matrix A is called a:", listOf("Non-singular matrix", "Singular matrix", "Identity matrix", "Scalar matrix"), 1, "A square matrix whose determinant is zero (|A| = 0) is called a Singular Matrix, and its inverse does not exist."),
                McqQuestion("mcq_bm1_2", "BM_1", "Simple Interest on Rs. 10,000 for 2 years at 10% per annum is:", listOf("Rs. 1,000", "Rs. 2,000", "Rs. 2,100", "Rs. 12,000"), 1, "Using I = P × r × t = 10,000 × 0.10 × 2 = Rs. 2,000."),
                McqQuestion("mcq_bm1_3", "BM_1", "The degree of a Quadratic Equation is always:", listOf("1", "2", "3", "0"), 1, "A quadratic equation has the highest power (exponent) of the variable equal to 2 (ax² + bx + c = 0).")
            )
        ),

        // ==================== I.COM PART 2 ====================
        IComSubject(
            code = "ACC_2",
            title = "Principles of Accounting II",
            urduSubtitle = "اصولِ محاسبہ (سالِ دوم)",
            part = "PART_2",
            totalMarks = 100,
            passingMarks = 33,
            keyTopics = listOf("Single Entry System", "Non-Profit Organizations", "Consignment Accounts", "Depreciation", "Partnership & Company Accounts"),
            description = "Advanced intermediate accounting: Accounts from Incomplete Records (Single Entry), Income & Expenditure Account, Consignment, Depreciation methods, Partnership Admission/Retirement/Dissolution, and Joint Stock Company Share & Debenture issuance.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Accounts from Incomplete Records (Single Entry)",
                    summary = "Determining profit or loss under the Net Worth (Statement of Affairs) method by comparing Opening Capital and Closing Capital.",
                    keyFormulasOrRules = listOf(
                        "Statement of Affairs: Capital = Total Assets - External Liabilities",
                        "Adjusted Closing Capital = Closing Capital + Drawings - Fresh/Additional Capital",
                        "Net Profit = Adjusted Closing Capital - Opening Capital - Interest on Capital"
                    ),
                    importantQuestions = listOf(
                        "Differentiate between Single Entry System and Double Entry System.",
                        "What is a Statement of Affairs and how does it differ from a Balance Sheet?"
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Non-Profit Making Organizations",
                    summary = "Accounting for clubs, hospitals, libraries, and colleges using Receipts & Payments Account, Income & Expenditure Account, and Balance Sheet.",
                    keyFormulasOrRules = listOf(
                        "Receipts & Payments A/c = Summary of Cash Book (starts with Opening Cash, ends with Closing Cash)",
                        "Income & Expenditure A/c = Equivalent to Profit & Loss A/c (Revenue items only on accrual basis)",
                        "Credit Balance of Income & Expenditure = Surplus | Debit Balance = Deficit",
                        "Capital Fund = Opening Total Assets - Opening Liabilities"
                    ),
                    importantQuestions = listOf(
                        "Distinguish between Receipts & Payments Account and Income & Expenditure Account.",
                        "Explain the accounting treatment of Legacy, Life Membership Fee, Entrance Fee, and Subscriptions."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Consignment Accounts & Depreciation",
                    summary = "Consignor and Consignee ledger accounts, Normal vs Abnormal Loss, Valuation of Unsold Stock, and Depreciation via Straight Line & Diminishing Balance Methods.",
                    keyFormulasOrRules = listOf(
                        "Value of Unsold Consignment Stock = Proportionate Cost + Proportionate Consignor's Direct Expenses + Consignee's Non-Recurring (Direct) Expenses",
                        "Straight Line Depreciation = (Cost of Asset - Scrap Value) / Estimated Useful Life",
                        "Diminishing Balance Method: Depreciation charged at fixed % on Written Down Value (Book Value) each year"
                    ),
                    importantQuestions = listOf(
                        "Differentiate between Consignment and Sale. What are Account Sales and Del-Credere Commission?",
                        "Compare Fixed Installment (Straight Line) Method and Diminishing Balance Method of Depreciation."
                    )
                ),
                SubjectChapter(
                    number = 4,
                    title = "Partnership & Joint Stock Company Accounts",
                    summary = "Goodwill valuation, Revaluation Account on Admission/Retirement of Partner, and Issue of Shares & Debentures (at Par, Premium, and Discount).",
                    keyFormulasOrRules = listOf(
                        "Sacrificing Ratio = Old Ratio - New Ratio (used on Admission of a new partner)",
                        "Gaining Ratio = New Ratio - Old Ratio (used on Retirement/Death of a partner)",
                        "Share Issue at Premium: Bank A/c (Dr) To Share Capital A/c (Cr) To Share Premium A/c (Cr)"
                    ),
                    importantQuestions = listOf(
                        "What is Goodwill? Explain the Average Profit Method and Super Profit Method of valuing Goodwill.",
                        "Define Shares and Debentures, and pass journal entries for issuance of Debentures at Par, Premium, and Discount."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_acc2_1", "ACC_2", "PART_2", "Formula for Net Profit in Single Entry (Net Worth Method)?", "Net Profit = Closing Capital + Drawings - Fresh Capital - Opening Capital", "Single Entry"),
                BuiltInFlashcard("fc_acc2_2", "ACC_2", "PART_2", "What is Del-Credere Commission in Consignment?", "Extra commission paid by the Consignor to the Consignee for bearing the risk of bad debts on credit sales.", "Consignment"),
                BuiltInFlashcard("fc_acc2_3", "ACC_2", "PART_2", "Formula for Sacrificing Ratio?", "Sacrificing Ratio = Old Profit Sharing Ratio - New Profit Sharing Ratio", "Partnership"),
                BuiltInFlashcard("fc_acc2_4", "ACC_2", "PART_2", "What does Surplus mean in an Income & Expenditure Account?", "Excess of Revenue Income over Revenue Expenditure for a Non-Profit Organization (added to Capital Fund).", "Non-Profit")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_acc2_1", "ACC_2", "Income and Expenditure Account is prepared by:", listOf("Trading concerns", "Manufacturing firms", "Non-Profit Organizations", "Banks only"), 2, "Non-trading / Non-Profit Organizations (clubs, hospitals, schools) prepare an Income & Expenditure Account to find Surplus or Deficit."),
                McqQuestion("mcq_acc2_2", "ACC_2", "In Consignment, the relationship between Consignor and Consignee is that of:", listOf("Buyer and Seller", "Debtor and Creditor", "Principal and Agent", "Partners"), 2, "The Consignor is the Principal (owner of goods) and the Consignee acts as an Agent selling goods on commission."),
                McqQuestion("mcq_acc2_3", "ACC_2", "Under the Diminishing Balance Method, the amount of annual depreciation:", listOf("Remains constant every year", "Decreases every year", "Increases every year", "Becomes zero in 2nd year"), 1, "Because a fixed percentage is applied to the reducing Book Value (Written Down Value), annual depreciation decreases each year."),
                McqQuestion("mcq_acc2_4", "ACC_2", "Share Premium received by a company is shown in the Balance Sheet under:", listOf("Current Assets", "Reserves / Capital Reserves (Liabilities side)", "Profit & Loss Account", "Fixed Assets"), 1, "Share Premium is a capital profit and is shown on the Equity & Liabilities side under Reserves and Surplus.")
            )
        ),

        IComSubject(
            code = "BLAW_2",
            title = "Principles of Banking",
            urduSubtitle = "اصولِ بینکاری (سالِ دوم)",
            part = "PART_2",
            totalMarks = 75,
            passingMarks = 25,
            keyTopics = listOf("Evolution & Kinds of Banks", "Commercial Banks & Credit Creation", "Central Bank & Monetary Policy", "Bank Accounts & Cheques", "Negotiable Instruments"),
            description = "Complete curriculum of Banking in Pakistan: Origin of Banking, Commercial Banks, Process of Credit Creation, State Bank of Pakistan (Central Bank) functions & Monetary Policy, Banker-Customer relationship, and Crossing/Endorsement of Cheques.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Origin, Evolution & Classification of Banks",
                    summary = "Origin from Goldsmiths, Money Lenders, and Merchants; Scheduled vs Non-Scheduled Banks, Commercial, Central, Industrial, Agricultural (ZTBL), and Islamic Banking.",
                    keyFormulasOrRules = listOf(
                        "Primary Functions of Commercial Bank: (1) Accepting Deposits (Current, Savings, Fixed), (2) Advancing Loans",
                        "Credit Creation Multiplier = 1 / Cash Reserve Ratio (CRR)",
                        "Total Credit Created = Primary Deposit × (1 / Reserve Ratio)"
                    ),
                    importantQuestions = listOf(
                        "Explain the primary, secondary (agency), and general utility functions of a Commercial Bank.",
                        "How do Commercial Banks create credit? Explain the process and limitations of Credit Creation.",
                        "Differentiate between a Scheduled Bank and a Non-Scheduled Bank."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Central Bank (State Bank of Pakistan) & Monetary Policy",
                    summary = "Monopoly of Note Issue (Proportional vs Minimum Reserve System), Banker's Bank, Lender of Last Resort, Clearing House, and Quantitative/Qualitative methods of Credit Control.",
                    keyFormulasOrRules = listOf(
                        "State Bank of Pakistan (SBP) inaugurated by Quaid-e-Azam on 1st July 1948",
                        "Quantitative Tools: Bank Rate Policy, Open Market Operations (OMO), Cash Reserve Ratio (CRR)",
                        "Qualitative Tools: Moral Suasion, Credit Rationing, Direct Action, Margin Requirements"
                    ),
                    importantQuestions = listOf(
                        "Explain the main functions of a Central Bank (State Bank of Pakistan).",
                        "What is Monetary Policy? Explain the various methods of Credit Control used by the Central Bank."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Banker-Customer Relationship & Negotiable Instruments",
                    summary = "Opening bank accounts, general and special relationships between Banker and Customer, Cheques (Bearer, Order, Crossed), Dishonor of Cheque, and Bill of Exchange vs Promissory Note.",
                    keyFormulasOrRules = listOf(
                        "General Relationship: Debtor and Creditor (when customer deposits money, Bank is Debtor & Customer is Creditor)",
                        "Types of Crossing: General Crossing ('& Co.', 'Not Negotiable') vs Special Crossing (Name of specific bank written)",
                        "Reasons for Dishonor: Insufficient funds (N.S.F), Post-dated/Stale cheque (>6 months), Signature mismatch"
                    ),
                    importantQuestions = listOf(
                        "Discuss the rights and duties of a Banker and Customer.",
                        "What is Crossing of a Cheque? Explain the types of Crossing and under what circumstances a bank dishonors a cheque."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_bank2_1", "BLAW_2", "PART_2", "When was the State Bank of Pakistan (SBP) established?", "1st July, 1948 (inaugurated by Quaid-e-Azam Muhammad Ali Jinnah).", "Central Bank"),
                BuiltInFlashcard("fc_bank2_2", "BLAW_2", "PART_2", "Formula for Total Credit Creation?", "Total Credit Created = Initial Primary Deposit × (1 / Cash Reserve Ratio).", "Credit Creation"),
                BuiltInFlashcard("fc_bank2_3", "BLAW_2", "PART_2", "What is a Stale Cheque?", "A cheque whose validity period (6 months from the date written on it) has expired.", "Negotiable Instruments")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_bank2_1", "BLAW_2", "When a customer deposits money in a bank, the relationship between Customer and Banker is:", listOf("Debtor and Creditor", "Creditor and Debtor", "Principal and Agent", "Bailor and Bailee"), 1, "The Customer becomes the Creditor and the Bank becomes the Debtor because the bank owes the deposited money back on demand."),
                McqQuestion("mcq_bank2_2", "BLAW_2", "Which bank has the sole monopoly of Note Issue in Pakistan?", listOf("National Bank of Pakistan", "Habib Bank Limited", "State Bank of Pakistan", "Zari Taraqiati Bank"), 2, "The State Bank of Pakistan (Central Bank) holds the sole monopoly of issuing currency notes in Pakistan."),
                McqQuestion("mcq_bank2_3", "BLAW_2", "If initial deposit is Rs. 10,000 and Cash Reserve Ratio is 20%, total deposits created will be:", listOf("Rs. 20,000", "Rs. 50,000", "Rs. 80,000", "Rs. 100,000"), 1, "Multiplier = 1 / 0.20 = 5. Total Deposits = 10,000 × 5 = Rs. 50,000.")
            )
        ),

        IComSubject(
            code = "CG_2",
            title = "Commercial Geography",
            urduSubtitle = "تجارتی جغرافیہ (سالِ دوم)",
            part = "PART_2",
            totalMarks = 75,
            passingMarks = 25,
            keyTopics = listOf("World Economic Activities", "Agriculture & Cash Crops", "Minerals & Energy Resources", "Major Industries", "Geography of Pakistan"),
            description = "World and Pakistan Commercial Geography: Primary to Quaternary economic activities, Wheat, Rice, Cotton, Sugarcane, Tea, Iron & Steel, Cotton Textile Industry, and Pakistan's Physiography, Irrigation (Indus Basin), and Ports.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Introduction to Commercial Geography & Economic Activities",
                    summary = "Classification of human economic activities into Primary (Agriculture, Fishing, Mining), Secondary (Manufacturing), Tertiary (Trade, Transport, Banking), Quaternary, and Quinary.",
                    keyFormulasOrRules = listOf(
                        "Primary Activities: Direct extraction from nature (Farming, Forestry, Fishing, Mining)",
                        "Secondary Activities: Value addition by transforming raw materials into finished goods (Textiles, Steel)",
                        "Tertiary Activities: Services supporting production & distribution (Banking, Transport, Trade)"
                    ),
                    importantQuestions = listOf(
                        "Define Economic and Commercial Geography and explain its importance for commerce students.",
                        "Classify human economic activities into five categories with examples."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "World & Pakistan Agriculture (Wheat, Rice, Cotton, Sugarcane)",
                    summary = "Geographical conditions (Temperature, Rainfall, Soil, Labor) and global/Pakistan distribution of major food and cash crops.",
                    keyFormulasOrRules = listOf(
                        "Pakistan Cropping Seasons: Rabi (Sown Oct-Dec, Harvested Apr-May: Wheat, Gram) & Kharif (Sown Apr-Jun, Harvested Oct-Dec: Cotton, Rice, Sugarcane)",
                        "Cotton (Silver Fiber): Requires 25°C-35°C, 200 frost-free days, alluvial soil",
                        "Wheat: Staple food crop of Pakistan; Punjab is the chief producer"
                    ),
                    importantQuestions = listOf(
                        "Describe the favorable geographical factors required for the cultivation of Cotton or Wheat, and name major producing areas in Pakistan.",
                        "Explain the Canal Irrigation System of Pakistan and its importance for agriculture."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Manufacturing Industries & Minerals (Iron, Coal, Petroleum)",
                    summary = "Localization factors and global distribution of Cotton Textile Industry, Iron & Steel Industry, Sugar Industry, and major energy/mineral resources.",
                    keyFormulasOrRules = listOf(
                        "Types of Coal by Carbon Content: Anthracite (highest quality, >85%), Bituminous, Lignite, Peat (lowest)",
                        "Pakistan Major Ports: Karachi Port, Port Muhammad Bin Qasim, Gwadar Deep Sea Port"
                    ),
                    importantQuestions = listOf(
                        "What factors govern the localization of the Cotton Textile Industry? Discuss its importance in Pakistan.",
                        "Write a detailed note on Iron & Steel Industry or Sugar Industry in Pakistan."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_cg2_1", "CG_2", "PART_2", "Difference between Rabi and Kharif crops in Pakistan?", "Rabi crops are winter crops sown in Oct-Dec and harvested in April-May (e.g., Wheat). Kharif crops are summer crops sown in April-June and harvested in Oct-Dec (e.g., Cotton, Rice, Sugarcane).", "Pakistan Agriculture"),
                BuiltInFlashcard("fc_cg2_2", "CG_2", "PART_2", "Which type of Coal has the highest carbon content?", "Anthracite (contains 85% to 95% carbon, burns with a smokeless blue flame).", "Minerals"),
                BuiltInFlashcard("fc_cg2_3", "CG_2", "PART_2", "What are Pakistan's three major seaports?", "Karachi Port, Port Muhammad Bin Qasim, and Gwadar Port.", "Pakistan Trade")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_cg2_1", "CG_2", "Wheat in Pakistan is a:", listOf("Kharif crop", "Rabi crop", "Zaid Kharif crop", "Plantation crop"), 1, "Wheat is sown in winter (October–December) and harvested in spring (April–May), making it the main Rabi crop."),
                McqQuestion("mcq_cg2_2", "CG_2", "Which is the finest quality of coal with the highest carbon percentage?", listOf("Peat", "Lignite", "Bituminous", "Anthracite"), 3, "Anthracite is the hardest and highest-ranking coal with ~90% carbon content."),
                McqQuestion("mcq_cg2_3", "CG_2", "Agriculture, Fishing, and Mining belong to which sector of economic activity?", listOf("Primary activity", "Secondary activity", "Tertiary activity", "Quaternary activity"), 0, "Activities that extract raw materials directly from nature are Primary economic activities.")
            )
        ),

        IComSubject(
            code = "STAT_2",
            title = "Business Statistics",
            urduSubtitle = "تجارتی شماریات (سالِ دوم)",
            part = "PART_2",
            totalMarks = 50,
            passingMarks = 17,
            keyTopics = listOf("Presentation of Data", "Measures of Central Tendency", "Index Numbers", "Probability"),
            description = "Statistical analysis for commerce: Primary vs Secondary data, Frequency Distribution, Mean, Median, Mode, Price Index Numbers (Laspeyres, Paasche, Fisher), and Basic Probability.",
            chapters = listOf(
                SubjectChapter(
                    number = 1,
                    title = "Collection & Presentation of Data",
                    summary = "Primary and Secondary data sources, Classification, Tabulation, Frequency Distribution (Class Limits, Class Boundaries, Midpoint/Class Mark), Histograms, and Frequency Polygons.",
                    keyFormulasOrRules = listOf(
                        "Range (R) = Maximum Value (X_max) - Minimum Value (X_min)",
                        "Class Mark (Midpoint X) = (Lower Class Limit + Upper Class Limit) / 2",
                        "Relative Frequency = Class Frequency (f) / Total Frequency (Σf)"
                    ),
                    importantQuestions = listOf(
                        "Define Statistics and explain its importance in Business and Commerce.",
                        "Differentiate between Primary Data and Secondary Data, and state methods of collecting Primary Data."
                    )
                ),
                SubjectChapter(
                    number = 2,
                    title = "Measures of Central Tendency (Mean, Median, Mode)",
                    summary = "Computation and properties of Arithmetic Mean (Direct & Shortcut methods), Median, and Mode for ungrouped and grouped frequency distributions.",
                    keyFormulasOrRules = listOf(
                        "Arithmetic Mean (Ungrouped): X̄ = ΣX / n  |  (Grouped): X̄ = ΣfX / Σf",
                        "Median (Grouped Data): l + (h / f) × (n/2 - C)",
                        "Mode (Grouped Data): l + [(f_m - f_1) / ((f_m - f_1) + (f_m - f_2))] × h",
                        "Empirical Relation: Mode = 3 Median - 2 Mean"
                    ),
                    importantQuestions = listOf(
                        "State the qualities of a good average (measure of central tendency).",
                        "Calculate Arithmetic Mean, Median, and Mode from a grouped frequency distribution table."
                    )
                ),
                SubjectChapter(
                    number = 3,
                    title = "Index Numbers & Probability",
                    summary = "Simple and Weighted Aggregative Index Numbers, Laspeyres, Paasche, and Fisher's Ideal Index Number, Consumer Price Index (CPI), and Classical Probability.",
                    keyFormulasOrRules = listOf(
                        "Laspeyres Index (Base Year Quantity): (Σ p_n q_0 / Σ p_0 q_0) × 100",
                        "Paasche Index (Current Year Quantity): (Σ p_n q_n / Σ p_0 q_n) × 100",
                        "Fisher's Ideal Index = √(Laspeyres × Paasche)",
                        "Probability P(A) = m / n (Favorable Outcomes / Total Sample Space Outcomes), where 0 ≤ P(A) ≤ 1"
                    ),
                    importantQuestions = listOf(
                        "Compute Laspeyres, Paasche, and Fisher's Ideal Price Index Numbers from the given price and quantity data.",
                        "Define Sample Space, Mutually Exclusive Events, and Independent Events with examples."
                    )
                )
            ),
            builtInFlashcards = listOf(
                BuiltInFlashcard("fc_stat2_1", "STAT_2", "PART_2", "Formula for Fisher's Ideal Index Number?", "Fisher's Ideal Index = √(Laspeyres Index × Paasche Index) = √[(Σp_n q_0 / Σp_0 q_0) × (Σp_n q_n / Σp_0 q_n)] × 100", "Index Numbers"),
                BuiltInFlashcard("fc_stat2_2", "STAT_2", "PART_2", "What is the sum of deviations of values from their Arithmetic Mean: Σ(X - X̄)?", "Always equal to Zero: Σ(X - X̄) = 0.", "Central Tendency"),
                BuiltInFlashcard("fc_stat2_3", "STAT_2", "PART_2", "What is the range of Probability P(A)?", "Probability always lies between 0 and 1 inclusive: 0 ≤ P(A) ≤ 1.", "Probability")
            ),
            builtInMcqs = listOf(
                McqQuestion("mcq_stat2_1", "STAT_2", "The sum of deviations of observations from their Arithmetic Mean Σ(X - X̄) is always:", listOf("Minimum", "One", "Zero", "Maximum"), 2, "It is a fundamental mathematical property of Arithmetic Mean that the algebraic sum of deviations from the mean is always zero."),
                McqQuestion("mcq_stat2_2", "STAT_2", "The Geometric Mean of Laspeyres and Paasche Index Numbers is called:", listOf("Marshall-Edgeworth Index", "Fisher's Ideal Index", "Simple Aggregative Index", "Chain Base Index"), 1, "Fisher's Ideal Index Number is the square root of the product (Geometric Mean) of Laspeyres and Paasche indices."),
                McqQuestion("mcq_stat2_3", "STAT_2", "In a Base Year, the value of the Price Index Number is always taken as:", listOf("0", "1", "100", "1000"), 2, "By convention, the base period index is always expressed as 100%.")
            )
        )
    )

    fun getSubjectsByPart(part: String): List<IComSubject> =
        subjects.filter { it.part == part }

    fun getSubjectByCode(code: String): IComSubject? =
        subjects.find { it.code == code }
}

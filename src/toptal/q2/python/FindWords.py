def findWords(rules):
    ruleDict = dict()
    revRuleDict = dict()
    for rule in rules:
        ruleDict[rule[0]] = rule[2]
        revRuleDict[rule[2]] = rule[0]
    rule = rules[0]
    left = rule[0]
    right = rule[2]
    word = left+right
    keys = ruleDict.keys()
    while(right in keys):
        word += ruleDict[right]
        right = ruleDict[right]

    revKeys = revRuleDict.keys()
    while(left in revKeys):
        word = revRuleDict[left]+word
        left = revRuleDict[left]
    return word


print(findWords(["P>E", "E>R", "R>U"]) == "PERU")
print(findWords(["I>N", "A>I", "P>A", "S>P"]) == "SPAIN")
print(findWords(["U>N", "G>A", "R>Y", "H>U", "N>G", "A>R"]) == "HUNGARY")
print(findWords(["W>I", "R>L", "T>Z", "Z>E", "S>W", "E>R",
                 "L>A", "A>N", "N>D", "I>T"]) == "SWITZERLAND")

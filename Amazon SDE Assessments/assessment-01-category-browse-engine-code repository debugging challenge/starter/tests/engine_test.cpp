#include "browse/engine.hpp"
#include "browse/parser.hpp"

#include <functional>
#include <iostream>
#include <set>
#include <sstream>
#include <string>
#include <vector>

using namespace browse;

namespace {

int failures = 0;

void expect(bool condition, const std::string& message) {
    if (!condition) {
        ++failures;
        std::cerr << "  FAILED: " << message << '\n';
    }
}

std::vector<Category> base_categories() {
    const std::string json = R"({
      "categories": [
        {"category_id":"ELEC","name":"Electronics","parent_id":null},
        {"category_id":"ELEC-MOB","name":"Cell Phones & Accessories","parent_id":"ELEC"},
        {"category_id":"ELEC-MOB-UNL","name":"Unlocked Cell Phones","parent_id":"ELEC-MOB"},
        {"category_id":"ELEC-MOB-ACC","name":"Accessories","parent_id":"ELEC-MOB"},
        {"category_id":"ELEC-MOB-CASE","name":"Cases","parent_id":"ELEC-MOB"},
        {"category_id":"ELEC-CAM","name":"Camera & Photo","parent_id":"ELEC"},
        {"category_id":"ELEC-CAM-DSLR","name":"DSLR Cameras","parent_id":"ELEC-CAM"},
        {"category_id":"ELEC-CAM-MIR","name":"Mirrorless Cameras","parent_id":"ELEC-CAM"},
        {"category_id":"ELEC-SEC","name":"Security & Surveillance","parent_id":"ELEC"},
        {"category_id":"ELEC-SEC-CAM","name":"Surveillance Cameras","parent_id":"ELEC-SEC"},
        {"category_id":"HOME","name":"Home & Kitchen","parent_id":null},
        {"category_id":"HOME-KIT","name":"Kitchen & Dining","parent_id":"HOME"},
        {"category_id":"HOME-FURN","name":"Furniture","parent_id":"HOME"},
        {"category_id":"HOME-FURN-BED","name":"Bedroom Furniture","parent_id":"HOME-FURN"}
      ]
    })";
    return parse_categories_json(json);
}

std::vector<Product> base_products() {
    const std::vector<std::string> lines = {
        R"({"product_id":"P1001","name":"Samsung Galaxy S24 Ultra","category_id":"ELEC-MOB-UNL","region":"US","popularity_score":96})",
        R"({"product_id":"P1002","name":"Google Pixel 9 Pro","category_id":"ELEC-MOB-UNL","region":"US","popularity_score":93})",
        R"({"product_id":"P1003","name":"OnePlus 12","category_id":"ELEC-MOB-UNL","region":"IN","popularity_score":91})",
        R"({"product_id":"P1004","name":"Samsung Galaxy S24 Ultra","category_id":"ELEC-MOB-UNL","region":"IN","popularity_score":95})",
        R"({"product_id":"P1005","name":"OtterBox Defender Series","category_id":"ELEC-MOB-CASE","region":"US","popularity_score":91})",
        R"({"product_id":"P1006","name":"Canon EOS R6 Mark II","category_id":"ELEC-CAM-MIR","region":"US","popularity_score":94})",
        R"({"product_id":"P1007","name":"Sony Alpha a7 IV","category_id":"ELEC-CAM-MIR","region":"US","popularity_score":92})",
        R"({"product_id":"P1008","name":"Nikon D850","category_id":"ELEC-CAM-DSLR","region":"US","popularity_score":92})",
        R"({"product_id":"P1009","name":"Ring Indoor Cam","category_id":"ELEC-SEC-CAM","region":"US","popularity_score":90})",
        R"({"product_id":"P1010","name":"Kindle Paperwhite","category_id":"ELEC","region":"US","popularity_score":94})",
        R"({"product_id":"P1011","name":"Ring Indoor Cam","category_id":"ELEC-SEC-CAM","region":"IN","popularity_score":86})",
        R"({"product_id":"P1012","name":"Instant Pot Duo 7-in-1","category_id":"HOME-KIT","region":"US","popularity_score":97})",
        R"({"product_id":"P1013","name":"Ninja Air Fryer Pro","category_id":"HOME-KIT","region":"US","popularity_score":95})",
        R"({"product_id":"P1014","name":"Zinus Memory Foam Mattress","category_id":"HOME-FURN-BED","region":"US","popularity_score":92})"
    };

    std::vector<Product> products;
    for (const std::string& line : lines) {
        products.push_back(parse_product_jsonl_line(line));
    }
    return products;
}

BrowseResult evaluate_one(const std::string& request_json) {
    const Engine engine = Engine::from_categories(base_categories());
    const Request request = parse_request_jsonl_line(request_json);
    return engine.evaluate({request}, base_products()).front();
}

void test_parser_maps_product_fields() {
    const Product product = parse_product_jsonl_line(
        R"({"product_id":"P9000","name":"Sample Product","category_id":"ELEC","region":"US","popularity_score":88})");
    expect(product.product_id == "P9000", "product_id must be read from product_id");
    expect(product.name == "Sample Product", "name must be read from name");
    expect(product.popularity_score == 88, "popularity_score must be parsed");
}

void test_top_level_includes_all_descendants() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q1","category_id":"ELEC","region":"US","max_results":20})");
    expect(result.matched_count == 8, "ELEC/US should match 8 products");
    std::set<std::string> ids;
    for (const ResultProduct& product : result.products) ids.insert(product.product_id);
    expect(ids.count("P1001") && ids.count("P1002") && ids.count("P1005"),
           "mobile descendants must be included");
    expect(ids.count("P1006") && ids.count("P1007") && ids.count("P1008"),
           "camera descendants must be included");
    expect(ids.count("P1009") && ids.count("P1010"),
           "security descendants and root products must be included");
}

void test_mid_level_includes_subtree_only() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q2","category_id":"ELEC-MOB","region":"US","max_results":20})");
    expect(result.matched_count == 3, "ELEC-MOB/US should match exactly 3 products");
    std::set<std::string> ids;
    for (const ResultProduct& product : result.products) ids.insert(product.product_id);
    expect(ids == std::set<std::string>({"P1001", "P1002", "P1005"}),
           "mid-level browse must exclude sibling subtrees");
}

void test_region_filter_exact_match() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q3","category_id":"ELEC-MOB-UNL","region":"IN","max_results":10})");
    expect(result.matched_count == 2, "IN filter should return only two IN products");
    expect(result.products.size() == 2, "both matching products should be returned");
    if (result.products.size() == 2) {
        expect(result.products[0].product_id == "P1004", "higher popularity should rank first");
        expect(result.products[1].product_id == "P1003", "second IN product should follow");
    }
}

void test_region_with_no_matches_returns_empty() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q4","category_id":"ELEC","region":"EU","max_results":10})");
    expect(result.matched_count == 0, "unknown region should have zero eligible products");
    expect(result.products.empty(), "zero matches must return an empty product list");
}

void test_ranking_popularity_desc_then_id_asc() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q5","category_id":"ELEC-CAM","region":"US","max_results":3})");
    expect(result.products.size() == 3, "camera query should return three products");
    if (result.products.size() == 3) {
        expect(result.products[0].product_id == "P1006", "94-point product should rank first");
        expect(result.products[1].product_id == "P1007", "tie must use product_id ascending");
        expect(result.products[2].product_id == "P1008", "larger tied ID should follow");
    }
}

void test_default_max_results_truncates_with_correct_count() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q6","category_id":"ELEC","region":"US"})");
    expect(result.matched_count == 8, "matched_count must be calculated before truncation");
    expect(result.products.size() == 5, "missing max_results must default to 5");
}

void test_unknown_category_returns_empty() {
    const BrowseResult result = evaluate_one(
        R"({"query_id":"q7","category_id":"DOES-NOT-EXIST","region":"US","max_results":10})");
    expect(result.matched_count == 0, "unknown category must have zero matches");
    expect(result.products.empty(), "unknown category must return an empty list");
}

}  // namespace

int main() {
    int failed_tests = 0;
    const std::vector<std::pair<std::string, std::function<void()>>> tests = {
        {"parser_maps_product_fields", test_parser_maps_product_fields},
        {"top_level_includes_all_descendants", test_top_level_includes_all_descendants},
        {"mid_level_includes_subtree_only", test_mid_level_includes_subtree_only},
        {"region_filter_exact_match", test_region_filter_exact_match},
        {"region_with_no_matches_returns_empty", test_region_with_no_matches_returns_empty},
        {"ranking_popularity_desc_then_id_asc", test_ranking_popularity_desc_then_id_asc},
        {"default_max_results_truncates_with_correct_count", test_default_max_results_truncates_with_correct_count},
        {"unknown_category_returns_empty", test_unknown_category_returns_empty}
    };

    for (const auto& test : tests) {
        const int before = failures;
        std::cout << "[ RUN      ] " << test.first << '\n';
        test.second();
        if (failures == before) {
            std::cout << "[       OK ] " << test.first << '\n';
        } else {
            ++failed_tests;
            std::cout << "[  FAILED  ] " << test.first << '\n';
        }
    }

    std::cout << "\n" << (tests.size() - static_cast<std::size_t>(failed_tests))
              << " tests passed, " << failed_tests << " tests failed\n";
    return failed_tests == 0 ? 0 : 1;
}

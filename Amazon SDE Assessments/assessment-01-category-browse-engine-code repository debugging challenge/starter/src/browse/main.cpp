#include "browse/engine.hpp"
#include "browse/file_io.hpp"

#include <exception>
#include <iostream>
#include <string>
#include <vector>

int main(int argc, char** argv) {
    std::string categories_path;
    std::string products_path;
    std::string requests_path;
    std::string output_path;

    for (int i = 1; i < argc; ++i) {
        const std::string argument = argv[i];
        if (argument == "--categories" && i + 1 < argc) {
            categories_path = argv[++i];
        } else if (argument == "--products" && i + 1 < argc) {
            products_path = argv[++i];
        } else if (argument == "--requests" && i + 1 < argc) {
            requests_path = argv[++i];
        } else if (argument == "--out" && i + 1 < argc) {
            output_path = argv[++i];
        }
    }

    if (categories_path.empty() || products_path.empty() ||
        requests_path.empty() || output_path.empty()) {
        std::cerr << "--categories, --products, --requests, and --out are required\n";
        return 2;
    }

    try {
        const std::vector<browse::Category> categories =
            browse::read_categories(categories_path);
        const std::vector<browse::Product> products =
            browse::read_products(products_path);
        const std::vector<browse::Request> requests =
            browse::read_requests(requests_path);

        const browse::Engine engine = browse::Engine::from_categories(categories);
        const std::vector<browse::BrowseResult> results =
            engine.evaluate(requests, products);
        browse::write_results(output_path, results);
        return 0;
    } catch (const std::exception& error) {
        std::cerr << "error: " << error.what() << '\n';
        return 1;
    }
}

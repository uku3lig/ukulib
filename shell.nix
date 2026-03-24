let
  pkgs = import <nixpkgs> { };
in
pkgs.mkShellNoCC {
  packages = with pkgs; [
    python3Packages.mkdocs-material
    prettier
  ];
}

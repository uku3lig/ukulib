let
  pkgs = import <nixpkgs> { };

  autoMavenVersion = pkgs.python3Packages.buildPythonPackage {
    pname = "auto-maven-version-plugin";
    version = "0.1.0";
    src = ./docs/plugins/auto_maven_version;
    pyproject = true;
    build-system = [ pkgs.python3Packages.setuptools ];
  };
in
pkgs.mkShellNoCC {
  packages = with pkgs; [
    (pkgs.python3.withPackages (p: [
      p.mkdocs-material
      autoMavenVersion
    ]))
    prettier
  ];
}
